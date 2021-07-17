package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import platform.Service.CodeService;
import platform.entity.Code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CodeHTMLController {

    @Autowired
    private CodeService codeService;

    @GetMapping("/code/new")
    public String postCodeHtml(Model model) {
        return "postHtml";
    }

    @GetMapping("/code/{N}")
    public String getOneCode(@PathVariable String N, Model model) {

        Code code = codeService.findCodeById(N);
        model.addAttribute("code", code.getCode());
        model.addAttribute("date", code.getDate());
        if (code.isRestrictionTime() && code.isRestrictionViews()) {
            model.addAttribute("views", code.getViews() );
            model.addAttribute("time", code.getRemainTime());
            return "getNRestriction";
        } else {
            if (code.isRestrictionViews()) {
                model.addAttribute("views", code.getViews() );
                return "getNViewRestriction";
            }
            if (code.isRestrictionTime()) {
                model.addAttribute("time", code.getRemainTime());
                return "getNTimeRestrioction";
            }
            return "getNHtml";
        }
    }

    @GetMapping("/code/latest")
    public String getLatestCodes(Model model) {
        List<Code> latestCodes = codeService.findLatest10Code();
        model.addAttribute("codes", latestCodes);
        return "getLatest10";
    }
}
