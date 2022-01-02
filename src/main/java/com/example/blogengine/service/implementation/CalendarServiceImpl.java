package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.CalenderResponse;
import com.example.blogengine.model.Post;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final PostRepository postRepository;

    @Override
    public CalenderResponse getCalendar(String year) {
        List<Post> postList = postRepository.findAllPosts();

        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");

        Set<String> years = new TreeSet<>();
        Map<String, Integer> posts = new TreeMap<>();

        String currentYear = simpleDateFormatYear.format(new Date());

        if (year.equals("none")) {
            year = currentYear;
        }

        for (Post p : postList) {
            String yearDate = simpleDateFormatYear.format(p.getTime());
            years.add(yearDate);
            String date = simpleDateFormatDate.format(p.getTime());

            if (yearDate.equals(year)) {
                if (posts.containsKey(date)) {
                    posts.put(date, posts.get(date) + 1);
                } else {
                    posts.put(date, 1);
                }
            }
        }

        return new CalenderResponse(years, posts);
    }
}
