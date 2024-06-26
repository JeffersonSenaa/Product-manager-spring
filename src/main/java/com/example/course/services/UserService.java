package com.example.course.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.course.entities.User;
import com.example.course.repositories.UserRepository;
import com.example.course.services.exceptions.DatabaseException;
import com.example.course.services.exceptions.ResourceNotFoundExceptions;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

		@Autowired
		private UserRepository repository;
		
		public List<User> findAll(){
			return repository.findAll();
		}
		
		public User findById(Long id) {
			Optional<User> obj = repository.findById(id);
			return obj.orElseThrow(() -> new ResourceNotFoundExceptions(id));
		}
		
		public User insert(User user) {
			return repository.save(user);
		}
		
		public void delete(Long id) {
			try {
				repository.deleteById(id);
			} catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundExceptions(id);
			} catch(DataIntegrityViolationException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		
		public User update(Long id, User user) {
			try {
				User entity = repository.getReferenceById(id);
				updateData(entity, user);
				return repository.save(entity);
			} catch (EntityNotFoundException e){
				throw new ResourceNotFoundExceptions(id);
			}
		}

		private void updateData(User entity, User user) {
			entity.setName(user.getName());
			entity.setEmail(user.getEmail());
			entity.setPhone(user.getPhone());			
		}
		
		
}
