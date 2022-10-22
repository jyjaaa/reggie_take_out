package com.chen.reggie.controller5;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.reggie.commmon6.R;
import com.chen.reggie.entity1.Employee;
import com.chen.reggie.service3.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.PanelUI;
import java.time.LocalDateTime;

/**
 * @author chen
 * @create 2022/9/26 16:58
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**
         *员工登录方法的业务逻辑
         *  1.将页面提交的密码password进行md5加密处理
         *  2. 根据页面提交的用户名username查询数据库
         *  3. 没查询到则返回登录失败结果
         *  4. 密码比对，不一致则返回登录失败结果
         *  5. 查看员工状态，如果为禁用状态则返回员工已禁用结果
         *  6. 登录成功，将员工ID存入session并返回登录成功结果
         *
         *  @param @RequestBody 传入的是json 故需要将其转化为实体类，json中的类名与实体类名对应才可以封装
         *  A. 由于需求分析时, 我们看到前端发起的请求为post请求, 所以服务端需要使用注解 @PostMapping
         *  B. 由于前端传递的请求参数为json格式的数据, 这里使用Employee对象接收, 但是将json格式数据封装到实体类中, 在形参前需要加注解@RequestBody
         */

//        1.将页面提交的密码password进行md5加密处理, 得到加密后的字符串
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        2. 根据页面提交的用户名username查询数据库中员工数据信息
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();//包装查询对象
        queryWrapper.eq(Employee::getUsername,employee.getUsername());//等值查询
        Employee emp = employeeService.getOne(queryWrapper);//username唯一约束获得结果


//        3. 没查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

//        4. 密码比对，不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

//        5. 查看员工状态，如果为禁用状态则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

//         6. 登录成功，将员工ID存入session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

//        清理session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工: 用于保存用户员工信息
     *在组装员工信息时,还需要封装创建时间、修改时间，
     * 创建人、修改人信息(从session中获取当前登录用户)。
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        // A. 在新增员工时，设置初始密码，需要进行MD5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//// B. 在组装员工信息时, 需封装创建时间、修改时间，创建人、修改人信息(从session中获取当前登录用户)
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获得当前登录用户的id
//        Long empId =(Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId); // 创建员工信息的人
//        employee.setUpdateUser(empId); // 最后一次更新信息的人

        employeeService.save(employee); // 因为employeeService继承了，所以不用写，直接用


        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * 1). 页面发送ajax请求，将分页查询参数(page、pageSize、name)提交到服务端
     * 2). 服务端Controller接收页面提交的数据, 并组装条件调用Service查询数据
     * 3). Service调用Mapper操作数据库，查询分页数据
     * 4). Controller将查询到的分页数据, 响应给前端页面
     * 5). 页面接收到分页数据, 并通过ElementUI的Table组件展示到页面上
     *
     * @param page 当前查询页码
     * @param pageSize 每页展示记录数
     * @param name 员工姓名 - 可选参数
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        //添加过滤条件 ：name不为空，添加过滤条件，取值
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询 两个参数
        employeeService.page(pageInfo,queryWrapper);//结果自动放入 pageInfo

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * 5、页面接收服务端响应的json数据，通过VUE的数据绑定进行员工信息回显
     * 6、点击保存按钮，发送ajax请求，将页面中的员工信息以json方式提交给服务端
     * 7、服务端接收员工信息，并进行处理，完成后给页面响应
     * 8、页面接收到服务端响应信息后进行相应处理
     * -点击保存：因add.html页面为公共页面，新增员工和编辑员工都是在此页面操作，所以该代码部分与之前添加员工代码对应，不需要重写。
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());//查看employee对象的属性（String类型）

//        //获得当前线程用户id（执行插入更新操作时会自动更新id）
//        long id = Thread.currentThread().getId();
//        log.info("线程id为：{}",id);

//        Long empId =(Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * 1、点击编辑按钮时，页面跳转到add.html，并在url中携带参数[员工id]
     * 2、在add.html页面获取url中的参数[员工id]
     * 3、发送ajax请求，请求服务端，同时提交员工id参数
     * 4、服务端接收请求，根据员工id查询员工信息，将员工信息以json形式响应给页面
     *  --操作如下：
     *  -回显：根据客户端返回的id值进行查询，然后返回查询到的信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}") //请求路径为具体id地址加{}
    public R<Employee> getById(@PathVariable Long id){//请求路径为具体id地址加@PathVariable
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("没查到对应员工信息");

    }
}
