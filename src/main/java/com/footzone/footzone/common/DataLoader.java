package com.footzone.footzone.common;

import com.footzone.footzone.entity.attachment.Attachment;
import com.footzone.footzone.entity.attachment.AttachmentRepository;
import com.footzone.footzone.entity.date.Date;
import com.footzone.footzone.entity.date.DateRepository;
import com.footzone.footzone.entity.footballSession.Session;
import com.footzone.footzone.entity.footballSession.SessionRepository;
import com.footzone.footzone.entity.role.Role;
import com.footzone.footzone.entity.role.RoleRepository;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.entity.stadium.StadiumRepository;
import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.entity.time.TimeRepository;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import com.footzone.footzone.entity.workingDay.WorkingDay;
import com.footzone.footzone.entity.workingDay.WorkingDayRepository;
import com.footzone.footzone.enums.Status;
import com.footzone.footzone.enums.WeekDay;
import com.footzone.footzone.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final StadiumRepository stadiumRepository;

    private final SessionRepository sessionRepository;

    private final DateRepository dateRepository;

    private final WorkingDayRepository workingDayRepository;

    private final TimeRepository timeRepository;

    @Value("${spring.sql.init.mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {


            Role admin = roleRepository.save(new Role(
                    AppConstants.ADMIN,
                    "admin"
            ));
            Role user = roleRepository.save(new Role(
                    AppConstants.USER,
                    "User"
            ));
            Role stadiumHolder = roleRepository.save(new Role(
                    AppConstants.STADIUM_HOLDER,
                    "Stadium Holder"
            ));

            Role superAdmin = roleRepository.save(new Role(
                    AppConstants.SUPER_ADMIN,
                    "System Holder"
            ));

//          Saving default attachment

            userRepository.save(new User(
                    "SuperAdmin",
                    "Super admin",
                    passwordEncoder.encode("admin123"),
                    new HashSet<>(
                            Arrays.asList(
                                    superAdmin,
                                    admin,
                                    stadiumHolder,
                                    user
                            )
                    ),
                    true,
                    new Attachment(
                            "default-profile-pic.jpg",
                            241,
                            "jpg",
                            "default-profile-pic.jpg"
                    )
            ));
            userRepository.save(new User(
                    "StadiumHolder",
                    "stadium",
                    passwordEncoder.encode("stadium123"),
                    new HashSet<>(
                            Arrays.asList(
                                    stadiumHolder,
                                    user
                            )
                    ),
                    true,
                    new Attachment(
                            "default-profile-pic.jpg",
                            241,
                            "jpg",
                            "default-profile-pic.jpg"
                    )
            ));

            userRepository.save(new User(
                    "Admin",
                    "99999999",
                    passwordEncoder.encode("admin123"),
                    new HashSet<>(
                            Arrays.asList(
                                    admin,
                                    stadiumHolder,
                                    user
                            )
                    ),
                    true,
                    new Attachment(
                            "default-profile-pic.jpg",
                            241,
                            "jpg",
                            "default-profile-pic.jpg"
                    )
            ));

            userRepository.save(new User(
                    "User",
                    "+998993885322",
                    passwordEncoder.encode("user123"),
                    new HashSet<>(
                            Collections.singletonList(
                                    user
                            )
                    ),
                    true,
                    new Attachment(
                            "default-profile-pic.jpg",
                            241,
                            "jpg",
                            "default-profile-pic.jpg"
                    )
            ));

            createSessionsAndStadiums();
            roleRepository.createFunction();
        }
    }

    private void createSessionsAndStadiums() {

        Time savedTime = timeRepository.save(new Time(LocalTime.of(9, 0)));
        Time savedTime2 = timeRepository.save(new Time(LocalTime.of(23, 59)));

        WorkingDay monday = workingDayRepository.save(new WorkingDay(WeekDay.MONDAY, savedTime, savedTime2));
        WorkingDay tuesday = workingDayRepository.save(new WorkingDay(WeekDay.TUESDAY, savedTime, savedTime2));
        WorkingDay wednesday = workingDayRepository.save(new WorkingDay(WeekDay.WEDNESDAY, savedTime, savedTime2));
        WorkingDay thursday = workingDayRepository.save(new WorkingDay(WeekDay.THURSDAY, savedTime, savedTime2));
        WorkingDay friday = workingDayRepository.save(new WorkingDay(WeekDay.FRIDAY, savedTime, savedTime2));
        WorkingDay saturday = workingDayRepository.save(new WorkingDay(WeekDay.SATURDAY, savedTime, savedTime2));
        WorkingDay sunday = workingDayRepository.save(new WorkingDay(WeekDay.SUNDAY, savedTime, savedTime2));
        ArrayList<WorkingDay> workingDays = new ArrayList<>(Arrays.asList(monday, tuesday, wednesday, thursday, friday, saturday, sunday));

        Stadium stadium1 = stadiumRepository.save(
                new Stadium("Etihad Stadium", "+998901234567", 100000, "Some description",
                        true, null, false, 69.4342, 41.2334, null, workingDays));
        Stadium stadium2 = stadiumRepository.save(
                new Stadium("Novza1", "+998901234567", 20000, "Some description",
                        true, null, false, 69.4523, 41.4533, null, workingDays));
        Stadium stadium3 = stadiumRepository.save(
                new Stadium("Novza2", "+998901234567", 20000, "Some description",
                        true, null, false, 69.3476, 41.4576, null, workingDays));

        Date date1 = dateRepository.save(new Date(LocalDate.of(2022, 6, 9)));
        Date date2 = dateRepository.save(new Date(LocalDate.of(2022, 7, 9)));
        Time time1 = timeRepository.save(new Time(LocalTime.of(19, 0, 0)));
        Time time2 = timeRepository.save(new Time(LocalTime.of(21, 0, 0)));

        Time time3 = timeRepository.save(new Time(LocalTime.of(9, 0, 0)));
        Time time4 = timeRepository.save(new Time(LocalTime.of(9, 30, 0)));
        Time time5 = timeRepository.save(new Time(LocalTime.of(10, 00, 0)));
        Time time6 = timeRepository.save(new Time(LocalTime.of(11, 00, 0)));
        Time time7 = timeRepository.save(new Time(LocalTime.of(13, 00, 0)));
        Time time8 = timeRepository.save(new Time(LocalTime.of(14, 30, 0)));


        Session session1 = sessionRepository.save(new Session(stadium1, date1, time1, time2, Status.ACCEPTED, 200000, null));
        Session session2 = sessionRepository.save(new Session(stadium2, date2, time1, time2, Status.ACCEPTED, 40000, null));
        Session session3 = sessionRepository.save(new Session(stadium3, date2, time1, time2, Status.ACCEPTED, 40000, null));

        Session session4 = sessionRepository.save(new Session(stadium1, date2, time3, time4, Status.ACCEPTED, 40000, null));
        Session session5 = sessionRepository.save(new Session(stadium1, date2, time5, time6, Status.ACCEPTED, 40000, null));
        Session session6 = sessionRepository.save(new Session(stadium1, date2, time7, time8, Status.ACCEPTED, 40000, null));



    }
}
