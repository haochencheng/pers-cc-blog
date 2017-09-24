package pers.platform.blog.model;

/**
 * ��ҳModel��
 * 
 * @author
 *
 */
public class PageBean {

    private Integer currentPage; // 当前页
    private Integer pageSize; // 每页记录数
    private Integer totalPages; // 总页数
    private Integer start; // 每页起始记录数

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = (int) (totalPages % pageSize == 0
                ? totalPages / pageSize : totalPages / pageSize + 1);
    }

    public Integer getStart() {
        return this.start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public PageBean(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.start = (currentPage - 1) * pageSize;
    }

}
