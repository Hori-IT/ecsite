package com.example.ecsite.model.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecsite.model.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

	@Query(value="select * from purchase p " +
			"where created_at = (" + 
			"select max(created_at) from purchase p where p.user_id = :userId)",
			nativeQuery = true)
	List<Purchase> findHistory(@Param("userId") long userId);
	
	@Query(value ="insert into purchase (user_id,goods_id,goods_name,item_count,total,created_at)" +
			"values(?1,?2,?3,?4,?5,now())",nativeQuery = true)
	
	@Transactional
	@Modifying
	void persist(	@Param("userId") long userId,
					@Param("goodsId") long productId,
					@Param("goodsName") String goodsName,
					@Param("itemCount") long itemCount,
					@Param("total") long total);
}
