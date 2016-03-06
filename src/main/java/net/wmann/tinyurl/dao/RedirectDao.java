package net.wmann.tinyurl.dao;

import net.wmann.tinyurl.entity.RedirectEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RedirectDao extends JpaRepository<RedirectEntity, String>{

}
