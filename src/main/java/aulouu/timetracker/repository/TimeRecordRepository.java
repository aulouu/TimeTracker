package aulouu.timetracker.repository;

import aulouu.timetracker.model.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    // Поиск по сотруднику и периоду (сортировка по времени начала от новых к старым)
    List<TimeRecord> findByEmployeeIdAndStartTimeBetweenOrderByStartTimeDesc(
            Long employeeId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

}
