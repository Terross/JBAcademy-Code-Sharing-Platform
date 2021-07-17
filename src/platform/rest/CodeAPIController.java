package platform.rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import platform.Service.CodeService;
import platform.entity.Code;
import org.json.simple.parser.JSONParser;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class CodeAPIController {

    @Autowired
    private CodeService codeService;

    @GetMapping("/api/code/{N}")
    public Code getCode(@PathVariable String N) {

        Code code = codeService.findCodeById(N);
        code.setTime(code.getRemainTime());
        return code;
    }

    @PostMapping("/api/code/new")
    public ResponseEntity<Object> postCode(@RequestBody String postJson) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(postJson);
        Code code = new Code((String) jsonObject.get("code"),
                Code.formatDate(LocalDateTime.now()),
                (Long) jsonObject.get("time"),
                (Long) jsonObject.get("views"));

        codeService.addCode(code);
        Map<String, String> json = new TreeMap();
        json.put("id", String.valueOf(code.getId()));
        return new ResponseEntity<>(new JSONObject(json), HttpStatus.OK);
    }

    @GetMapping("/api/code/latest")
    public ResponseEntity<Object> getLatest() throws InterruptedException {
        JSONArray jsonArray = new JSONArray();
        List<Code> latest10 = codeService.findLatest10Code();
        System.out.println(latest10.size());
        for (int i = 0; i < latest10.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", latest10.get(i).getCode());
                jsonObject.put("date", latest10.get(i).getDate());
                jsonObject.put("time", latest10.get(i).getTime());
                jsonObject.put("views", latest10.get(i).getViews());
                jsonArray.add(jsonObject);
        }
        return new ResponseEntity<>(jsonArray, HttpStatus.OK);
    }
}
