package aulouu.timetracker.controller;

import aulouu.timetracker.dto.requst.CreateTimeRecordRequest;
import aulouu.timetracker.dto.requst.TimeRecordPeriodRequest;
import aulouu.timetracker.dto.response.TimeRecordResponse;
import aulouu.timetracker.service.TimeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/time-records")
@RequiredArgsConstructor
@Validated
public class TimeRecordController {
    private final TimeRecordService timeRecordService;

    @PostMapping
    public TimeRecordResponse createTimeRecord(@Valid @RequestBody CreateTimeRecordRequest request) {
        return timeRecordService.createTimeRecord(request);
    }

    @PostMapping("/period")
    public List<TimeRecordResponse> getTimeRecordsByPeriod(@Valid @RequestBody TimeRecordPeriodRequest request) {
        return timeRecordService.getTimeRecordsByPeriod(request);
    }
}
