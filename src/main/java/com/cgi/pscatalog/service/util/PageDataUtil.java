package com.cgi.pscatalog.service.util;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageDataUtil<T, U> {

	private Page<T> pageInformation;

	private List<U> content;

	public Page<T> getPageInformation() {
		return pageInformation;
	}

	public void setPageInformation(Page<T> pageInformation) {
		this.pageInformation = pageInformation;
	}

	public List<U> getContent() {
		return content;
	}

	public void setContent(List<U> content) {
		this.content = content;
	}

}
