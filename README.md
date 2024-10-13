# Illumino
This program parses a flow log file, maps each flow record to a corresponding tag using a lookup table, and generates reports with the following:

. Tag Counts: Number of matches for each tag based on destination port and protocol combinations.
. Port/Protocol Combination Counts: Number of matches for each port and protocol combination.
Input Files
Flow Log File: A plain text (ASCII) file containing flow log records. Each row contains log details, including source/destination IP addresses, ports, protocol, and action (ACCEPT/REJECT).
Sample flow log format:
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK

Lookup Table (CSV): A CSV file with three columns: dstport, protocol, and tag. The dstport and protocol combination determines the tag that can be applied to each log record.
   Example:
   dstport,protocol,tag
    25,tcp,sv_P1
    443,tcp,sv_P2
Output Files
Tag Counts: A report listing the total number of flow records matching each tag.
 Example:
Tag   Count
sv_P2  1
sv_P1  2
email  3
Untagged 9
Port/Protocol Combination Counts: A report listing how many flow records match each unique port and protocol combination.
 Example:
Port/Protocol   Count
443/tcp         1
25/tcp          1
. Assumptions
The program only supports default log format (version 2) and does not handle custom formats.
The matches for destination port and protocol combinations are case-insensitive.
Any flow log records that do not match a tag in the lookup table will be classified as "Untagged."
Flow log files can be up to 10 MB, and the lookup table can contain up to 10,000 mappings.
Compilation:
To compile the program, run the following command in the directory where FlowLogParser.java is located:
javac FlowLogParser.java
Running the Program
Once compiled, you can run the program as follows:
java FlowLogParser <flow_log_file> <lookup_table_file> <output_directory>
. <flow_log_file>: Path to the input flow log file.
. <lookup_table_file>: Path to the CSV file containing the lookup table.
. <output_directory>: Directory where the output files will be generated.
Example: java FlowLogParser flowlogs.txt lookup.csv output/






