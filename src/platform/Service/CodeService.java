package platform.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import platform.entity.Code;
import platform.repository.CodeRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CodeService {

    @Autowired
    CodeRepository codeRepository;

    public void addCode(Code code) {
        codeRepository.save(code);
    }

    public Code findCodeById(String id) {
        Code code = codeRepository.findById(id).orElseThrow(IndexOutOfBoundsException::new);
        if (code.isRestrictionTime() || code.isRestrictionViews()) {
            LocalDateTime ldt = LocalDateTime.parse(code.getDate(),
                    DateTimeFormatter.ofPattern(Code.DATE_FORMATTER));
            if ((Duration.between(ldt, LocalDateTime.now()).toSeconds() > code.getTime() && code.isRestrictionTime())) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not Found"
                );
            } else {
                if ((code.getViews() <= 0 && code.isRestrictionViews())) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Not Found"
                    );
                } else {
                    if (code.isRestrictionViews()) {
                        code.setViews(code.getViews() - 1);
                        codeRepository.save(code);
                    }
                    if (code.isRestrictionTime()) {
                        code.setRemainTime(code.getTime() - Duration.between(ldt, LocalDateTime.now()).toSeconds());
                    }
                }
            }

        }

        return code;
    }

    public List<Code> findLatest10Code() {
        List<Code> result = new ArrayList<>();
        List<Code> codes = StreamSupport
                .stream(codeRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Code::getDate).reversed())
                .collect(Collectors.toList());
        int flag = 0;
        for (int i = 0; i < codes.size() && flag < 10; i++) {
            if (!codes.get(i).isRestrictionTime() && !codes.get(i).isRestrictionViews()) {
                result.add(codes.get(i));
                flag++;
            }
        }
        return result;
    }
}
