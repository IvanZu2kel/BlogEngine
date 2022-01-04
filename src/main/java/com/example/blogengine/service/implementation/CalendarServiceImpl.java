package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.settings.CalendarResponse;
import com.example.blogengine.model.Post;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final PostRepository postRepository;

    private final SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    public CalendarResponse getCalendar(String year) {
        Set<Integer> yearSet = new HashSet<>();
        Map<String, Integer> dateMap = new HashMap<>();
        List<Post> postList = postRepository.findPosts();
        if (year.equals(""))
            year = String.valueOf(LocalDate.now().getYear());
        String finalYear = year;
        postList.forEach(post -> {
            yearSet.add(Integer.valueOf(yearFormat.format(post.getTime())));
            if (finalYear.equals(yearFormat.format(post.getTime())))
                dateMap.compute(dayMonthYearFormat.format(post.getTime()), (k, v) -> (v == null) ? v = 1 : v + 1);
        });
        return new CalendarResponse()
                .setPosts(dateMap)
                .setYears(yearSet.toArray(new Integer[0]));
    }
}
