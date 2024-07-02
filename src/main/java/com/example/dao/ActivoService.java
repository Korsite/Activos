package com.example.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


@Service
public class ActivoService{


	private Page<ActivoModel> paginate(int page, int size, List<ActivoModel> activos){
		Pageable pageRequest = PageRequest.of(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), activos.size());
		List<ActivoModel> subList = activos.subList(start, end);
		return new PageImpl<>(subList, pageRequest, activos.size());
	}
	
	public Page<ActivoModel> getAll(int page, int size, ArrayList<ActivoModel> activos) {
		return paginate(page, size, activos);
	}
}