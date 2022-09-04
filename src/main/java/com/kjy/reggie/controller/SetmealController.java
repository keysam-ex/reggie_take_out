package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjy.reggie.common.CustomException;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Category;
import com.kjy.reggie.domain.Setmeal;
import com.kjy.reggie.domain.SetmealDish;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.dto.SetmealDto;
import com.kjy.reggie.service.CategoryService;
import com.kjy.reggie.service.SetmealDishService;
import com.kjy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:柯佳元
 * @version:2.7
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {


    @Autowired
    private SetmealService setmealService; //套餐

    @Autowired
    private SetmealDishService setmealDishService; //套餐里的每一个 菜品 和 套餐有对应关系


    @Autowired
    private CategoryService categoryService;

    /**
     * 添加套餐
     *
     * @param SetmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto SetmealDto) {

        setmealService.saveWithDish(SetmealDto);
        return R.success("添加成功");

    }

    /**
     * 套餐分页
     * 套餐父类需要处理
     *
     * @param allPage
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(AllPage allPage) {
        log.info(allPage.toString());
        Page<Setmeal> setmealPage = new Page<>(allPage.getPage(), allPage.getPageSize());
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(allPage != null && allPage.getName() != null, Setmeal::getName, allPage.getName()).orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPage, setmealLambdaQueryWrapper); // 将数据都封装到 page对象里面

        Page<SetmealDto> setmealDtoPage = new Page<>();


        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> setmealDtos = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String name = category.getName();
                setmealDto.setCategoryName(name);
            }
            return setmealDto;

        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtos);
        log.info(setmealDtoPage.getRecords().toString());
        return R.success(setmealDtoPage);

    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids={}", ids);
        setmealService.deleteWithDish(ids);
        return R.success("删除成功!");
    }

    //回显数据
    @GetMapping("/{id}")
    public R<SetmealDto> echo(@PathVariable(value = "id") Long id) {
        log.info("id={}", id);
        //通过 套餐 id 得到 套餐对应的信息
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //将数据拷贝给 setmealDto
        BeanUtils.copyProperties(setmeal, setmealDto);
        //设置 套餐所属种类名
        Category category = categoryService.getById(setmeal.getCategoryId());
        if (category != null) {
            String name = category.getName();
            setmealDto.setCategoryName(name);
        }
        //将套餐所关联的菜肴附上
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);

        return R.success(setmealDto);
    }

    //修改套餐数据
    @PutMapping
    public R<String> updateSetMeal(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto={}", setmealDto);
        setmealService.updateWithSetmealDish(setmealDto);
        return R.success("修改成功");
    }

    //修改  起售/禁售状态
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {


        List<Setmeal> list = setmealService.listByIds(ids);

        list.stream().map(item -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(list);
        return R.success("修改套餐状态成功!");


    }


    /**
     * 给前台套餐返回数据
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<SetmealDto>> list(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> setmealDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (setmealDto != null){
            setmealDtoLambdaQueryWrapper.eq(setmealDto.getCategoryId() != null, Setmeal::getCategoryId,setmealDto.getCategoryId())
                                        .eq(setmealDto.getStatus() != null,Setmeal::getStatus,setmealDto.getStatus())
                                        .orderByDesc(Setmeal::getUpdateTime);
            List<Setmeal> setmealList = setmealService.list(setmealDtoLambdaQueryWrapper);

            List<SetmealDto> setmealDtos  = setmealList.stream().map(item ->{
                SetmealDto setmealDto1 = new SetmealDto();
                BeanUtils.copyProperties(item,setmealDto1);
                return setmealDto1;
            }).collect(Collectors.toList());

            return R.success(setmealDtos);
        }
        return R.error("数据有误");

    }


}
