package vn.edu.ute.admin.service;
import org.springframework.data.domain.*;
import java.util.function.Function;
public final class PageSupport {
    private PageSupport() {
    }
    public static <T> Page<T> search(int page, Function<Pageable,Page<T>> query) {
        // Read the total before constructing a potentially overflowing JPA offset.
        Page<T> first=query.apply(PageRequest.of(0,10,Sort.by("id").descending()));
        int safePage=Math.max(0,Math.min(page,first.getTotalPages()-1));
        if(safePage==0) return first;
        return query.apply(PageRequest.of(safePage,10,Sort.by("id").descending()));
    }
}
