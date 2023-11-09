package com.lcwd.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.lcwd.electronic.store.dtos.PageableResponse;

public class Helper {

	public static <U,V>PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
		
		List<U> entity = page.getContent();
		List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).
				collect(Collectors.toList());
		PageableResponse<V> response = new PageableResponse<>();
		
		response.setContent(dtoList);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setIsLastPage(page.isLast());
		
		return response;
	}
	
	public static Sort getSorting(String sortBy, String sortDirection) {
		Sort sort = Sort.by(sortBy).ascending();
		if(sortDirection.equalsIgnoreCase("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		return sort;
	}
}
