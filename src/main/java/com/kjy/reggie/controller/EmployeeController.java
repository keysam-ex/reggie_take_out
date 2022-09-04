package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Employee;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //员工登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        String password = employee.getPassword();
        //1、将页面提交的密码password进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee e = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if (e == null) {
            return R.error("账号或密码有误!!!");
        }
        //4、密码比对，如果不--致则返回登录失败结果
        if (!e.getPassword().equals(password)) {
            return R.error("账号或密码有误!");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (e.getStatus() == 0) {
            return R.error("账号封禁中!");
        }

        //6、登录成功，将员Iid存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", e.getId());

        return R.success(e);

    }

    //员工退出登录
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("employee");
        return R.success("退出成功!");
    }


    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee) {
        log.info("新增员工的信息={}", employee); //状态数据库默认 0

        //给新增员工设置初始密码 123456 (需要 MD5 加密)
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        /*设置创建时间  已经 由 MyMetaObjectHandler 解决
        employee.setCreateTime(LocalDateTime.now());
        //设置更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建该用户人 的 id
        Long createUserId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(createUserId);
        //设置更新该用户人 的 id
        employee.setUpdateUser(createUserId);

        */

        return employeeService.save(employee) ? R.success("新增员工成功") : R.error("新增员工失败");
    }


    /**
     * 按照条件分页查询员工信息
     * page: this.page,
     * pageSize: this.pageSize,
     * name: this.input  //咱们自己将他设置默认值 =  "" 而不是 un...
     *
     * @param allPage 封装
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> pageByCondition(AllPage allPage) {
        //1、分页
        Page<Employee> page = new Page<>(allPage.getPage(), allPage.getPageSize());

        //2、查找 关键字
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(allPage.getName()),Employee::getName, allPage.getName());

        //3、按 更新时间进行排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(page, queryWrapper);

        return R.success(page);
    }


    /**
     * 根据用户id进行修改 [修改用户状态  和 修改信息通用]
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        log.info(employee.toString());

        //===================== 修改 用户 是否封禁 . 前端 会传来需要修改后的状态码

        /*设置创建时间
        employee.setCreateTime(LocalDateTime.now());
        //设置更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建该用户人 的 id
        Long createUserId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(createUserId);
        //设置更新该用户人 的 id
        employee.setUpdateUser(createUserId);
         */

        employeeService.updateById(employee);
        return R.success("账号信息修改成功");
    }


    /**
     * // 页面回显的详情接口
     * function queryEmployeeById (id) {
     * return $axios({
     * url: `/employee/${id}`,
     * method: 'get'
     * })
     * }
     *
     * @param id 根据 id 查询员工信息
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> echo(@PathVariable(value = "id") Long id) {
        log.info("要修改回显的员工ID号={}", id.toString());
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查到该员工信息");
    }


}
