package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Category;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增菜品的信息={}", category);
        categoryService.save(category);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(AllPage allPage) {
        log.info("菜品分类第{}页,一页显示{}条", allPage.getPage(), allPage.getPageSize());
        //分页
        Page<Category> page = new Page<>(allPage.getPage(), allPage.getPageSize());
        //排序,根据 sort
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(page, queryWrapper);
        return R.success(page);
    }

    /**
     * 根据菜品分类id删除菜品分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {

        log.info("要删除菜品的id={}", ids);
        categoryService.remove(ids);

        return R.success("删除菜品成功!");
    }


    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改的套餐信息={}", category);
        categoryService.updateById(category);
        return R.success("修改成功！");
    }


    /**
     * 根据条件查 分类数据
     * 查询 所有的菜品分类
     *
     * @param category 通用性更高
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getList(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加方法,并且按顺序及更新时间排序
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);


    }


}
