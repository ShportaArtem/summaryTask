package web.tag;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import modelView.ShippingView;

/**
 * Tag for sort flights
 * @author A.Shporta
 *
 */
public class SortTag extends TagSupport{
	
	private static final long serialVersionUID = 4916192782255838077L;
	private String sortType;
	
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	public int doStartTag() {
			HttpSession session = pageContext.getSession();
			@SuppressWarnings("unchecked")
			List<ShippingView> shippingsView = (List<ShippingView>) session.getAttribute("shippingsViews");
			switch(sortType) {
				case "id":
					shippingsView.sort(Comparator.comparing(ShippingView::getId));
					break;
				case "createTime":
					shippingsView.sort(Comparator.comparing(ShippingView::getCreationTimestamp));
					break;
				case "status":
					shippingsView.sort(Comparator.comparing(ShippingView::getStatus));
					break;
				default:
					break;
			}
			
		return SKIP_BODY;
	}
}
