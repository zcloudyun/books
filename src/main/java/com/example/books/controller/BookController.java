package com.example.books.controller;

import com.example.books.dao.BookDao;
import com.example.books.entity.Book;
import com.example.books.res.ResponseResult;
import com.example.books.res.ResultCode;
//import com.github.pagehelper.PageHelper;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


//图书控制器
@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookDao bookDao;

    // 统一响应数据格式就可以了，直接返回数据对象即可进行相应，后端发送后，浏览器前端接收到的都是 JSON 数据格式，
    // 查询所有图书
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseResult<List> list() {
        List<Book> books = bookDao.findAll();
        // 统一响应就可以了
        return ResponseResult.SUCCESS().setData(books);
    }

//   添加图书
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseResult add(@RequestBody Book book){

        System.out.println(book);

        Book row = bookDao.save(book);
        System.out.println(book);
        // 第二种方式
        ResponseResult<Object> result = new ResponseResult<>(ResultCode.SUCCESS);
        // 保存成功的对象
        result.setData(book);
        return result;
    }
//    根据id查询book实体
    @ResponseBody
    @RequestMapping("/preUpdate/{id}")
    public ResponseResult preUpdate(@PathVariable("id") Integer id)
    {

        return ResponseResult.SUCCESS().setData(bookDao.findById(id));
    }
//    修改图书
    @PostMapping(value = "/update")
    public ResponseResult update(@RequestBody Book book)
    {
        System.out.println(book);

        Book row = bookDao.save(book);
        ResponseResult<Object> result = new ResponseResult<>(ResultCode.SUCCESS);
        // 保存成功的对象
        result.setData(book);
        return result;
    }
    //    删除图书
    @PostMapping(value = "/delete/{id}")
    public ResponseResult<Boolean> delete(@PathVariable("id") Integer id)
    {
        System.out.println(id);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try{
            bookDao.deleteById(id);
            // 没有异常：删除成功。响应删除成功
            result.setResultCode(ResultCode.SUCCESS);
            result.setData(true);
        }catch (Exception e){
            // 出现异常，删除失败，
            result.setResultCode(ResultCode.FAIL).setData(false);
        }
        return result;
    }

//    根据条件动态查询
    @RequestMapping("/list2")
    public ResponseResult list2(Book book)
    {
        List<Book> bookList=bookDao.findAll(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate=cb.conjunction();
                if(book!=null)
                {
                    if(book.getName()!=null && !"".equals(book.getName()))
                    {
                        predicate.getExpressions().add(cb.like(root.get("name"),"%"+book.getName()+"%"));
                    }
                    if(book.getAuthor()!=null && !"".equals(book.getAuthor()))
                    {
                        predicate.getExpressions().add(cb.like(root.get("name"),"%"+book.getAuthor()+"%"));
                    }
                }
                return predicate;
            }
        });
        ResponseResult<Object> result = new ResponseResult<>(ResultCode.SUCCESS);
        // 保存成功的对象
        result.setData(bookList);
        return result;
    }
//    根据姓名查询
    @ResponseBody
    @RequestMapping("/query/{name}")
    public ResponseResult query(@PathVariable("name") String name)
    {
        List<Book> books = bookDao.findByName(name);
        return ResponseResult.SUCCESS().setData(books);

    }
//    随机显示
    @ResponseBody
    @RequestMapping("/randomlist")
    public ResponseResult randomList(String name)
    {
        List<Book> books = bookDao.randomList(8);
        return ResponseResult.SUCCESS().setData(books);
    }
}
