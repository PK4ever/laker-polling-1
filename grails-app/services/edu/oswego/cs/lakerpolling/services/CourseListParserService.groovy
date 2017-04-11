package edu.oswego.cs.lakerpolling.services

import edu.oswego.cs.lakerpolling.util.QueryResult
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile

class CourseListParserService {

    class ParseError {
        final static String MISSING_HEADER = "Missing required 'Email' or 'Username' header"
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

    private static final CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withAllowMissingColumnNames().withIgnoreSurroundingSpaces()

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
     * Parses the given CSV file as a course list of students. The first row in the CSV file must be a header row, which
     * must contain a header labeled either "Email" or "Username" (case insensitive). For each record in the file the
     * parser will either extract the student's email directly from the "Email" column or will generate it dynamically
     * using the student's Laker NetID extracted from the "Username" column. If a record does not contain a student email
     * then the parser will skip the record without throwing an exception.
     *
     * @param reader - a Reader object associated with a character stream for a CSV file containing a list of students
     * @return a list of student emails extracted from the CSV file
     */
    QueryResult<List<String>> parse(Reader reader, QueryResult<List<String>> result = new QueryResult<>()) {
        try {
            CSVParser parser = format.parse(reader)
            def columnIndexMap = parseHeader(parser.getHeaderMap())
            verifyHeaderFormat(columnIndexMap, result)
            parseRecords(columnIndexMap, parser, result)
        }catch (IllegalArgumentException e1) {
            e1.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
        } catch (Exception e2) {
            e2.printStackTrace()
            QueryResult.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, result)
        }
        result
    }

    private Map<Column, Integer> parseHeader(Map<String,Integer> headerIndexMap) {
        Map<Column, Integer> columnIndexMap = new HashMap<>()

        // associate the logical columns with the actual column index in the CSV based on the headers
        for (headerIndexEntry in headerIndexMap.entrySet()) {
            for (column in Column.values()) {
                if (columnIndexMap.containsKey(column)) {
                    continue
                }
                if (column.matches(headerIndexEntry.getKey())) {
                    columnIndexMap.put(column, headerIndexEntry.value)
                    break
                }
            }
        }
        columnIndexMap
    }

    private QueryResult<List<String>> verifyHeaderFormat(Map<Column,Integer> columnIndexMap, QueryResult result) {
        if (!result.success) {
            return result
        }

        if (columnIndexMap.isEmpty()) {
            QueryResult.fromHttpStatus(HttpStatus.BAD_REQUEST, result)
            result.message = ParseError.MISSING_HEADER
        }
        result
    }

    private QueryResult<List<String>> parseRecords(Map<Column, Integer> columnIndexMap, Iterable<CSVRecord> records, QueryResult result) {
        if (!result.success) {
            return result
        }

        List<String> emails = new ArrayList<>()
        for (CSVRecord record : records) {
            def maybeEmail = parseEmail(columnIndexMap, record)
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