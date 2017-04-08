package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.util.QueryResult
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile

class CourseListParserService {

    class ParseError {
        final static String MISSING_COLUMN = "Missing required 'Email' or 'Username' column"
    }

    private enum Column {
        EMAIL("Email"),
        LAKER_NET_ID("Username")

        private String header

        private Column(String header) {
            this.header = header
        }

        boolean matches(String header) {
            header.replaceAll("[^A-Za-z]", "").equalsIgnoreCase(this.header)
        }
    }

    private static class CourseListFormat {
        final Map<Column, Integer> columnIndexMap
        final boolean firstRowIsHeader

        CourseListFormat(Map<Column, Integer> columnIndexMap, boolean firstRowIsHeader) {
            this.columnIndexMap = columnIndexMap
            this.firstRowIsHeader = firstRowIsHeader
        }
    }

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT

    /**
     * Parses the given CSV file as a course list of students. The CSV file may contain a header row with a header
     * labeled either "Email" or "Username" (case insensitive). If the file does not contain a header row then the
     * parser will identify the Email column by searching the first row for data that ends with "@oswego.edu". For each
     * record in the file the parser will either extract the student's email directly from the "Email" column or will
     * generate it dynamically using the student's Laker NetID extracted from the "Username" column. If a record does
     * not contain a student email then the parser will skip the record without throwing an exception.
     *
     * @param file - a MultipartFile object associated with a CSV file containing a list of students
     * @return a QueryResult containing either a list of student emails extracted from the CSV file or an error
     */
    QueryResult<List<String>> parse(MultipartFile file, QueryResult<List<String>> result = new QueryResult<>()) {
        BufferedReader reader = null
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()))
            parse(reader, result)
        }  catch(Exception e) {
            e.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, result)
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch(IOException e) {
                e.printStackTrace()
            }
        }
        result
    }

     /**
     * Parses the given CSV file as a course list of students. The CSV file may contain a header row with a header
     * labeled either "Email" or "Username" (case insensitive). If the file does not contain a header row then the
     * parser will identify the Email column by searching the first row for data that ends with "@oswego.edu". For each
     * record in the file the parser will either extract the student's email directly from the "Email" column or will
     * generate it dynamically using the student's Laker NetID extracted from the "Username" column. If a record does
     * not contain a student email then the parser will skip the record without throwing an exception.
     *
     * @param reader - a Reader object associated with a character stream for a CSV file containing a list of students
     * @return a QueryResult containing either a list of student emails extracted from the CSV file or an error
     */
    QueryResult<List<String>> parse(Reader reader, QueryResult<List<String>> result = new QueryResult<>()) {
        try {
            List<CSVRecord> records = CSV_FORMAT.parse(reader).getRecords()
            CourseListFormat courseListFormat = parseHeader(records.get(0))
            verifyFormat(courseListFormat, result)
            parseRecords(courseListFormat, records, result)
        }catch (IllegalArgumentException e1) {
            e1.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
        } catch (Exception e2) {
            e2.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, result)
        }
        result
    }

    // Associate the logical columns with the actual column index in the CSV based either on the header row or the
    // format of the column data (i.e. a column containing email data will be mapped to the Email column)
    private CourseListFormat parseHeader(CSVRecord headerRow) {
        Map<Column, Integer> columnIndexMap = new HashMap<>()
        for (int i = 0; i < headerRow.size(); i++) {
            String header = headerRow.get(i)
            if (isOswegoEmail(header)) {
                columnIndexMap.put(Column.EMAIL, i)
                return new CourseListFormat(columnIndexMap, false)
            } else {
                mapColumnToIndex(columnIndexMap, header, i)
            }
        }
        new CourseListFormat(columnIndexMap, true)
    }

    private void mapColumnToIndex(Map<Column, Integer> columnIndexMap, String header, int columnIndex) {
        for (column in Column.values()) {
            if (columnIndexMap.containsKey(column)) {
                continue
            }
            if (column.matches(header)) {
                columnIndexMap.put(column, columnIndex)
                break
            }
        }
    }

    private static boolean isOswegoEmail(String str) {
        str.endsWith("@oswego.edu")
    }

    private QueryResult<List<String>> verifyFormat(CourseListFormat format, QueryResult result) {
        if (!result.success) {
            return result
        }

        if (format.columnIndexMap.isEmpty()) {
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            result.message = ParseError.MISSING_COLUMN
        }
        result
    }

    private QueryResult<List<String>> parseRecords(CourseListFormat format, Iterable<CSVRecord> records, QueryResult result) {
        if (!result.success) {
            return result
        }

        Iterator<CSVRecord> recordIterator = records.iterator()
        if (format.firstRowIsHeader && recordIterator.hasNext()) {
            recordIterator.next()
        }

        List<String> emails = new ArrayList<>()
        while (recordIterator.hasNext()) {
            def maybeEmail = parseEmail(format.columnIndexMap, recordIterator.next())
            if (maybeEmail.isPresent()) {
                emails.add(maybeEmail.get())
            }
        }
        result.data = emails
        result
    }

    private Optional<String> parseEmail(Map<Column, Integer> columnIndexMap, CSVRecord record) {
        def maybeEmail = getFromRecord(record, columnIndexMap, Column.EMAIL)
        if (!maybeEmail.isPresent()) {
            def maybeLakerNetID = getFromRecord(record, columnIndexMap, Column.LAKER_NET_ID)
            return maybeLakerNetID.isPresent() ? Optional.of("${maybeLakerNetID.get()}@oswego.edu") : Optional.empty()
        }
        return maybeEmail
    }

    // Returns an empty Optional if the value at the given index in the record is empty, just whitespace, or doesn't exist
    private Optional<String> getFromRecord(CSVRecord record, Map<Column, Integer> columnIndexMap, Column column) {
        def index = columnIndexMap.get(column)
        if (index != null) {
            return getFromRecord(record, index)
        }
        return Optional.empty()
    }

    // Returns an empty Optional if the value at the given index in the record is empty, just whitespace, or doesn't exist
    private static Optional<String> getFromRecord(CSVRecord record, int index) {
        if (index < record.size()) {
            String result = record.get(index)
            if (result != null && !result.trim().isEmpty()) {
                return Optional.of(result)
            }
        }
        return Optional.empty()
    }
}