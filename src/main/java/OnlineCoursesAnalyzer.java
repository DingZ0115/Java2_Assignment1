import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4], info[5],
                        Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
                        Integer.parseInt(info[9]), Integer.parseInt(info[10]), Double.parseDouble(info[11]),
                        Double.parseDouble(info[12]), Double.parseDouble(info[13]), Double.parseDouble(info[14]),
                        Double.parseDouble(info[15]), Double.parseDouble(info[16]), Double.parseDouble(info[17]),
                        Double.parseDouble(info[18]), Double.parseDouble(info[19]), Double.parseDouble(info[20]),
                        Double.parseDouble(info[21]), Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    public Map<String, Integer> getPtcpCountByInst() {
        return courses.stream().collect(
                Collectors.groupingBy(Course::getInstitution, Collectors.summingInt(Course::getParticipants))
        );
    }

    //2
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Map<String, Integer> unsortedMap = courses.stream().collect(
                Collectors.groupingBy(course -> course.getInstitution() + "-" + course.getSubject(), Collectors.summingInt(Course::getParticipants))
        );
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(unsortedMap.entrySet());
        entries.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    //3
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<List<String>>> courseListOfInstructorMap = new LinkedHashMap<>();
        for (Course course : courses) {
            String[] instructors = course.getInstructors().split(",");
            if (instructors.length == 1) {
                String instructorName = instructors[0].trim();
                if (!courseListOfInstructorMap.containsKey(instructorName)) {
                    List<List<String>> listOfCourse = new ArrayList<>();
                    List<String> independentCourse = new ArrayList<>();
                    independentCourse.add(course.getTitle());
                    listOfCourse.add(independentCourse);
                    listOfCourse.add(new ArrayList<>());
                    courseListOfInstructorMap.put(instructorName, listOfCourse);
                } else {
                    List<List<String>> listOfCourse = courseListOfInstructorMap.get(instructorName);
                    if (!listOfCourse.get(0).contains(course.getTitle())) {
                        listOfCourse.get(0).add(course.getTitle());
                    }
                }
            } else {
                for (String instructor : instructors) {
                    String instructorName = instructor.trim();
                    if (!courseListOfInstructorMap.containsKey(instructorName)) {
                        List<List<String>> listOfCourse = new ArrayList<>();
                        List<String> coCourse = new ArrayList<>();
                        coCourse.add(course.getTitle());
                        listOfCourse.add(new ArrayList<>());
                        listOfCourse.add(coCourse);
                        courseListOfInstructorMap.put(instructorName, listOfCourse);
                    } else {
                        List<List<String>> listOfCourse = courseListOfInstructorMap.get(instructorName);
                        if (!listOfCourse.get(1).contains(course.getTitle())) {
                            listOfCourse.get(1).add(course.getTitle());
                        }
                    }
                }
            }
        }
        // sort the course lists by alphabetical order
        for (List<List<String>> listOfCourse : courseListOfInstructorMap.values()) {
            for (List<String> courseList : listOfCourse) {
                Collections.sort(courseList);
            }
        }
        return courseListOfInstructorMap;
    }

    //4
    public List<String> getCourses(int topK, String by) {
        return null;
    }

    //5
    public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
        return null;
    }

    //6
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        return null;
    }
}

class Course {
    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    double averageAge;

    double averageMale;

    double averageDegree;

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public void setAverageDegree(double averageDegree) {
        this.averageDegree = averageDegree;
    }

    public void setAverageMale(double averageMale) {
        this.averageMale = averageMale;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public String getNumber() {
        return number;
    }

    public double getMedianAge() {
        return medianAge;
    }

    public double getPercentMale() {
        return percentMale;
    }

    public double getPercentDegree() {
        return percentDegree;
    }

    public String getInstitution() {
        return institution;
    }

    public int getParticipants() {
        return participants;
    }

    public String getSubject() {
        return subject;
    }

    public String getInstructors() {
        return instructors;
    }

    public String getTitle() {
        return title;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public double getPercentAudited() {
        return percentAudited;
    }

    public Course(String institution, String number, Date launchDate,
                  String title, String instructors, String subject,
                  int year, int honorCode, int participants,
                  int audited, int certified, double percentAudited,
                  double percentCertified, double percentCertified50,
                  double percentVideo, double percentForum, double gradeHigherZero,
                  double totalHours, double medianHoursCertification,
                  double medianAge, double percentMale, double percentFemale,
                  double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }
}