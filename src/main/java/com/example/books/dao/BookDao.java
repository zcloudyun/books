package com.example.books.dao;

import com.example.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookDao extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {
    @Query(value = "select * from book b where b.name like %?1%",nativeQuery = true)
    public List<Book> findByName(String name);
    @Query(value = "select * from book ORDER BY rand() limit ?1",nativeQuery = true)
    public List<Book> randomList(Integer id);
}
