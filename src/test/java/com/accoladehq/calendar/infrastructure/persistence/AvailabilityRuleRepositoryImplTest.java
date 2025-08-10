package com.accoladehq.calendar.infrastructure.persistence;

import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaAvailabilityRuleRepository;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AvailabilityRuleRepositoryImpl.class)
class AvailabilityRuleRepositoryImplTest {

    @Autowired
    private AvailabilityRuleRepositoryImpl availabilityRuleRepository;

    @Autowired
    private JpaAvailabilityRuleRepository jpaAvailabilityRuleRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        jpaAvailabilityRuleRepository.deleteAll();
        jpaAvailabilityRuleRepository.flush();
    }

    User buildValidUser() {
        return User.builder()
                .id(null)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .build();
    }

    AvailabilityRule buildValidRule(UUID userId) {
        return AvailabilityRule.builder()
                .id(null)
                .userId(userId)
                .weekday(LocalDate.now().getDayOfWeek())
                .duration(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .build();
    }

    @Test
    void testFindAllByUserId() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        AvailabilityRule rule = buildValidRule(user.getId());
        rule = jpaAvailabilityRuleRepository.save(rule);

        var rules = availabilityRuleRepository.findAllByUserId(user.getId());
        assertFalse(rules.isEmpty());
        assertEquals(1, rules.size());
        assertEquals(rule.getUserId(), rules.get(0).getUserId());
        assertEquals(rule.getWeekday(), rules.get(0).getWeekday());
        assertEquals(rule.getDuration(), rules.get(0).getDuration());
    }

    @Test
    void testFindAllIntervalsByUserIdAndDate() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        AvailabilityRule rule = buildValidRule(user.getId());
        jpaAvailabilityRuleRepository.save(rule);

        LocalDate date = LocalDate.now();
        Set<TimeInterval> intervals = availabilityRuleRepository.findAllIntervalsByUserIdAndDate(user.getId(), date);
        assertNotNull(intervals);
        // Intervals should be a subset of the rule's duration
        assertTrue(intervals.stream().allMatch(i
                -> !i.getStartTime().isBefore(rule.getDuration().getStartTime())
                && !i.getEndTime().isAfter(rule.getDuration().getEndTime())
        ));
    }

    @Test
    void testSave() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        AvailabilityRule rule = buildValidRule(user.getId());
        AvailabilityRule savedRule = availabilityRuleRepository.save(rule);

        assertNotNull(savedRule.getId());
        assertEquals(rule.getUserId(), savedRule.getUserId());
        assertEquals(rule.getWeekday(), savedRule.getWeekday());
        assertEquals(rule.getDuration(), savedRule.getDuration());
    }

}
