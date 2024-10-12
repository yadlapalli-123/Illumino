package com.example;

import java.io.*;
import java.util.*;

public class FlowLogParser {

    public static void main(String[] args) {
        String lookupFilePath = "lookup.csv";
        String flowLogFilePath = "flow_logs.txt";
        Map<PortProtocol, String> lookupTable = loadLookupTable(lookupFilePath);
        Map<String, Integer> tagCounts = new HashMap<>();
        Map<PortProtocol, Integer> portProtocolCounts = new HashMap<>();

        parseFlowLogs(flowLogFilePath, lookupTable, tagCounts, portProtocolCounts);
        writeSummary(tagCounts, portProtocolCounts);
    }

    private static Map<PortProtocol, String> loadLookupTable(String filePath) {
        Map<PortProtocol, String> lookup = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int port = Integer.parseInt(parts[0]);
                String protocol = parts[1].toLowerCase(); // Case insensitive match
                String tag = parts[2];
                lookup.put(new PortProtocol(port, protocol), tag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lookup;
    }

    private static void parseFlowLogs(String filePath, Map<PortProtocol, String> lookupTable,
                                      Map<String, Integer> tagCounts,
                                      Map<PortProtocol, Integer> portProtocolCounts) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int dstPort = Integer.parseInt(parts[6]);
                String protocol = parts[7].toLowerCase();

                PortProtocol portProtocol = new PortProtocol(dstPort, protocol);
                String tag = lookupTable.getOrDefault(portProtocol, "Untagged");

                // Update tag counts
                tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                // Update port/protocol counts
                portProtocolCounts.put(portProtocol, portProtocolCounts.getOrDefault(portProtocol, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeSummary(Map<String, Integer> tagCounts, Map<PortProtocol, Integer> portProtocolCounts) {
        // Write tag counts
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tag_counts.csv"))) {
            bw.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write port/protocol counts
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("port_protocol_counts.csv"))) {
            bw.write("Port,Protocol,Count\n");
            for (Map.Entry<PortProtocol, Integer> entry : portProtocolCounts.entrySet()) {
                PortProtocol pp = entry.getKey();
                bw.write(pp.port + "," + pp.protocol + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class PortProtocol {
        int port;
        String protocol;

        PortProtocol(int port, String protocol) {
            this.port = port;
            this.protocol = protocol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PortProtocol that = (PortProtocol) o;
            return port == that.port && protocol.equals(that.protocol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(port, protocol);
        }
    }
}
