package fr.ffcam.pan.config;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

@ControllerAdvice
public class FreemarkerHelperAdvice {

	@ModelAttribute("show")
	public ShowMethod show() {
		return new ShowMethod();
	}
	class ShowMethod implements TemplateMethodModelEx {
		public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
			if (arguments.size() != 1) {
				throw new TemplateModelException("Wrong number of arguments");
			}
			
			Object o = DeepUnwrap.unwrap((TemplateModel) arguments.get(0));
			
			if (o instanceof Pageable) {
				return handlePageable((Pageable) o);
			}
			
			
			return o.toString();
		}
		
		private String handlePageable(Pageable pageable) {
			StringBuilder sb = new StringBuilder();
			
			sb
				.append("page=")
				.append(pageable.getPageNumber())
				.append("&")
				.append("size=")
				.append(pageable.getPageSize());
			
			for (Order order : pageable.getSort()) {
				sb
					.append("&")
					.append("sort=")
					.append(order.getProperty())
					.append(",")
					.append(order.getDirection().toString());
			}
			
			return sb.toString();
		}
	}
}
