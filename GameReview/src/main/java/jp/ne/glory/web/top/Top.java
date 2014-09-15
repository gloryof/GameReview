package jp.ne.glory.web.top;

import java.time.LocalDateTime;
import jp.ne.glory.web.top.bean.TopInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/top")
public class Top {

    @RequestMapping(method = RequestMethod.GET)
    public String test(final Model model) {

        final TopInfo topInfo = new TopInfo(LocalDateTime.now(), "Junki", "Yamada");

        model.addAttribute(topInfo);

        System.out.println("test");

        return "/top/top";
    }
}
