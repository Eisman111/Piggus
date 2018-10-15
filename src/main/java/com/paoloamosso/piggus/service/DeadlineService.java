/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * This is the bridge between the repository and the controller
 */

package com.paoloamosso.piggus.service;

import com.paoloamosso.piggus.dao.DeadlineRepository;
import com.paoloamosso.piggus.model.Deadline;
import com.paoloamosso.piggus.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("deadlineService")
public class DeadlineService {

    // == fields ==
    @Autowired
    @Qualifier("deadlineRepository")
    DeadlineRepository deadlinesRepository;
    private Calendar cal = Calendar.getInstance();
    private Calendar tempCal = Calendar.getInstance();

    // == public methods ==
    public void saveDeadline(Deadline deadline) {
        deadlinesRepository.save(deadline);
    }

    // Here we create the repeated deadlines
    // 0 as period means days
    // 1 as period means months
    // 2 as period means years
    public void createRepeatedDeadlines(Deadline deadline, int numberPeriod, int period, int repeatedTimes) {
        saveDeadline(deadline);
        for (int i = 1; i < repeatedTimes; i++) {
            Deadline repeatedDeadline = new Deadline(deadline);
            repeatedDeadline.setIdParentDeadline(deadline.getId());
            if (period == 0) {
                repeatedDeadline.setLocalDate(deadline.getLocalDate().plusDays(numberPeriod * i));
            } else if (period == 1) {
                repeatedDeadline.setLocalDate(deadline.getLocalDate().plusMonths(numberPeriod * i));
            } else if (period == 2) {
                repeatedDeadline.setLocalDate(deadline.getLocalDate().plusYears(numberPeriod * i));
            }
            saveDeadline(repeatedDeadline);
        }
    }

    // The parent always have the parent id equal to 0 but a son should be able to delete all the other son
    public void removeDeadline(Deadline deadline, int multipleRemove) {
        if (multipleRemove == 0) {
            deadlinesRepository.delete(deadline);
        } else {
            if (deadline.getIdParentDeadline() == 0) {
                deadlinesRepository.findDeadlinesByIdParentDeadlineAndArchivedFalse(deadline.getId()).stream()
                        .forEach(d -> deadlinesRepository.delete(d));
            } else {
                deadlinesRepository.findDeadlinesByIdParentDeadlineAndArchivedFalse(deadline.getIdParentDeadline()).stream()
                        .forEach(d -> deadlinesRepository.delete(d));
            }
        }
    }

    public List<Deadline> getDeadlinesList (User user, Boolean isArchived) {
        return deadlinesRepository.findDeadlinesByUser(user).stream()
                .filter(d -> d.isArchived() == isArchived)
                .collect(Collectors.toList());
    }

    // TODO refactor Priority 1: Find another way to manage this
    // Here we manage the list in the homepage where each month has a list of deadlines
    public Map<String, List<Deadline>> getDeadlines (User user, LocalDate localDate) {
        List<Deadline> deadlines = deadlinesRepository.findDeadlinesByUser(user).stream()
                .filter(d -> !d.isArchived())
                .collect(Collectors.toList());
        Map<String,List<Deadline>> deadlineListByMonth = new LinkedHashMap<>();
        int counter = 1;
        for (int i = localDate.getMonthValue(); i<=12; i++) {
            List<Deadline> temporaryDeadlines = new ArrayList<>();
            for (Deadline deadline : deadlines) {
                if (deadline.getLocalDate().getMonthValue() == i && deadline.getLocalDate().getYear() == localDate.getYear()) {
                    temporaryDeadlines.add(deadline);
                }
            }
            if (!temporaryDeadlines.isEmpty()) {
                deadlineListByMonth.put(getDate(i,localDate.getYear()),temporaryDeadlines);
            }
            counter++;
        }
        if (counter <12) {
            for (int i = 0; i<=(11-counter); i++) {
                List<Deadline> temporaryDeadlines = new ArrayList<>();
                for (Deadline deadline : deadlines) {
                    if (deadline.getLocalDate().getMonthValue() == i && deadline.getLocalDate().getYear() == (localDate.getYear() + 1)) {
                        temporaryDeadlines.add(deadline);
                    }
                }
                if (!temporaryDeadlines.isEmpty()) {
                    deadlineListByMonth.put(getDate(i,(localDate.getYear() + 1)),temporaryDeadlines);
                }
            }
        }
        return deadlineListByMonth;
    }

    // Get the deadline from the list of deadlines available to that user
    public Deadline getDeadline (int deadlineId) {
        return deadlinesRepository.findDeadlineById(deadlineId);
    }

    // This is a workaround to show on the front end the month and the year in a nice way
    private String getDate (int month, int year) {
        switch (month) {
            case 1:
                return "January " + year;
            case 2:
                return "February " + year;
            case 3:
                return "March " + year;
            case 4:
                return "April " + year;
            case 5:
                return "May " + year;
            case 6:
                return "June " + year;
            case 7:
                return "July " + year;
            case 8:
                return "August " + year;
            case 9:
                return "September " + year;
            case 10:
                return "October " + year;
            case 11:
                return "November " + year;
            case 12:
                return "December " + year;
            default:
                return null;
        }
    }
}
