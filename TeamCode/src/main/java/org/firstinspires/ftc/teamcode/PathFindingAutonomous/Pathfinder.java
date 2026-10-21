package org.firstinspires.ftc.teamcode.PathFindingAutonomous;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import java.util.ArrayList;



//funny imports for the Dijkstra algorithm.
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Set;


public class Pathfinder {

    static double boardLength = 144;//in 1in
    static double boardWidth = 144;//in 1in
    static double robotLength = 16.75;
    static double robotWidth = 16.75;
    static double paddingX = robotWidth/2;   //robot length / 2   The extra * 2 is for the 9 on each side. so robot doesn't hit it. Adding 1 after for a safety margin
    static double paddingY = robotLength/2;
    static double robotStartPosX = 0;  //this is in terms of 1in
    static double robotStartPosY = 0;  //this is in terms of 1in
    static double robotStartPosHeading;

    static double robotCenterX = robotStartPosX + (robotWidth / 2);
    static double robotCenterY = robotStartPosY + (robotLength / 2);

    static double nodeClearance = 3;//This spaces it slightly away from each rectangle so the nodes dont collide.


    //temp
    static double targetX = 10;
    static double targetY = 118;
    static double targetLength = 18;
    static double targetWidth = 18;
    static double targetCenterX = targetX + (targetWidth / 2);
    static double targetCenterY = targetY + (targetLength / 2);

    static Rectangle bottomBoundary = new Rectangle((int)boardLength, 0, 1, (int)boardWidth);
    static Rectangle rightBoundary = new Rectangle(0, (int)boardWidth, (int)boardLength, 1);
    static Rectangle topBoundary = new Rectangle(-1, 0, 1, (int)boardWidth);
    static Rectangle leftBoundary = new Rectangle(0, -1, (int)boardLength, 1);

    static Rectangle centerHiveLeg1 = new Rectangle(51, 46, 39, 4);
    static Rectangle centerHiveLeg2 = new Rectangle(51, 94, 39, 4);
    static Rectangle evilLine = new Rectangle(0, 72, 144, 0);
    static Rectangle flower1 = new Rectangle(0, 44, 5, 6);
    static Rectangle flower2 = new Rectangle(44, 139, 6, 5);
    static Rectangle flower3 = new Rectangle(139, 92, 5, 6);
    static Rectangle flower4 = new Rectangle(92, 0, 6, 5);
    static Rectangle redGarden = new Rectangle(0, 120, 3, 24);
    static Rectangle blueGarden = new Rectangle(141, 0, 3, 24);

    static ArrayList<Double> StopLeftBounds = new ArrayList<>();
    static ArrayList<Double> StopTopBounds = new ArrayList<>();
    static ArrayList<Double> StopRightBounds = new ArrayList<>();
    static ArrayList<Double> StopBottomBounds = new ArrayList<>();

    static ArrayList<Node> nodes = new ArrayList<>(); //For Djikstra's algorithm

    static ArrayList<Node> shortestPath = new ArrayList<>();//Shortest path from Djikstra's algorithm.



    public static Action pathfindToCoords(double x,double y, double heading, Pose2d startingPos, MecanumDrive drive) {
        targetX = x;//Yes, i know y is x, but that is bc par wheels track x motion, but I want it to track y.
        targetY = y;
        targetCenterX = targetX + (targetWidth / 2);
        targetCenterY = targetY + (targetLength / 2);

        // Use exact start coordinates from startingPos
        robotStartPosX = startingPos.position.x;
        robotStartPosY = startingPos.position.y;
        robotCenterX = robotStartPosX;
        robotCenterY = robotStartPosY;

        buildMap();

        // If shortestPath has 1 or 0 nodes, no trajectory can be generated
        if (shortestPath.size() <= 1) {
            return drive.actionBuilder(startingPos).build();
        }

        TrajectoryActionBuilder builder = drive.actionBuilder(startingPos);

        double finalHeading = Math.toRadians(heading);

        // Standard RoadRunner Coordinate System Note:
        // +X = Forward / Backward motion (lengthwise on field)
        // +Y = Left / Right motion (towards Blue side / left)

        // Loop through all target nodes after the starting node (index 0)
        for (int i = 1; i < shortestPath.size(); i++) {
            Node prev = shortestPath.get(i - 1);
            Node next = shortestPath.get(i);

            if (i == shortestPath.size() - 1) {
                // Final destination: rotate to final target heading
                builder = builder.strafeToLinearHeading(new Vector2d(next.x, next.y), finalHeading);
            } else {
                // Intermediate waypoints: face forward along travel direction
                double pathHeading = Math.atan2(next.y - prev.y, next.x - prev.x);
                builder = builder.strafeToLinearHeading(new Vector2d(next.x, next.y), pathHeading);
            }
        }

        return builder.build();
    }

    private static void buildMap() {

        nodes.clear();
        shortestPath.clear();

        StopLeftBounds.clear();
        StopTopBounds.clear();
        StopRightBounds.clear();
        StopBottomBounds.clear();

        getEStopBounds(topBoundary, rightBoundary, bottomBoundary, leftBoundary, centerHiveLeg1,centerHiveLeg2, evilLine, flower1, flower2, flower3, flower4,redGarden,blueGarden);

        //This sets node 0 to be the robot and node one to be the target, which is important for later.
        nodes.add(new Node(robotCenterX, robotCenterY)); //These two lines of code add nodes at the robot and target.
        nodes.add(new Node(targetCenterX, targetCenterY));//This is important for Djikstra's algorithm.

        //This for loop now adds the rest of the required nodes around each box.
        for (int i = 0; i < StopLeftBounds.size(); i++) {

            double left = StopLeftBounds.get(i);
            double top = StopTopBounds.get(i);
            double right = StopRightBounds.get(i);
            double bottom = StopBottomBounds.get(i);



            nodes.add(new Node(left - nodeClearance, top - nodeClearance));
            nodes.add(new Node(right + nodeClearance, top - nodeClearance));
            nodes.add(new Node(left - nodeClearance, bottom + nodeClearance));
            nodes.add(new Node(right + nodeClearance, bottom + nodeClearance));

        }

        buildGraph();

        //printGraph(); //Mainly used for debugging. Print's each node coordinate.

        //Uses the Dijkstra algorithm to grab the shortest path. And recall that robot is node 0 and target 1. ;)
        shortestPath = dijkstra(nodes.get(0), nodes.get(1));


        determinePathFind(robotStartPosX, robotStartPosY, targetX, targetY);

    }

    private static boolean lineIntersectsRectangle(
            double x1, double y1,
            double x2, double y2,
            double left, double top,
            double right, double bottom) {

        boolean startInside = (x1 >= left && x1 <= right && y1 >= top && y1 <= bottom);
        boolean endInside = (x2 >= left && x2 <= right && y2 >= top && y2 <= bottom);

        // Cannot move to a target node that is inside an obstacle yellow box
        if (endInside) {
            return true;
        }

        // If starting inside a yellow box (e.g. wall start position), allow driving OUT of it
        if (startInside) {
            return false;
        }

        // Standard boundary line intersection checks
        if (lineIntersectsLine(x1, y1, x2, y2, left, top, right, top)) return true;
        if (lineIntersectsLine(x1, y1, x2, y2, right, top, right, bottom)) return true;
        if (lineIntersectsLine(x1, y1, x2, y2, right, bottom, left, bottom)) return true;
        if (lineIntersectsLine(x1, y1, x2, y2, left, bottom, left, top)) return true;

        return false;
    }
    private static boolean lineIntersectsLine(
            double x1, double y1,
            double x2, double y2,
            double x3, double y3,
            double x4, double y4) {

        double denominator =
                (x1 - x2) * (y3 - y4) -
                        (y1 - y2) * (x3 - x4);

        if (denominator == 0) {
            return false;
        }

        double t =
                ((x1 - x3) * (y3 - y4) -
                        (y1 - y3) * (x3 - x4)) / denominator;

        double u =
                -((x1 - x2) * (y1 - y3) -
                        (y1 - y2) * (x1 - x3)) / denominator;

        return t >= 0 && t <= 1 && u >= 0 && u <= 1;
    }

    private static Rectangle stopBoundary(int startx, int starty, int width, int height) {
        return new Rectangle(startx, starty, width, height);
    }


    private static Rectangle expandedStopBoundary(Rectangle stopBoundary, double paddingX, double paddingY) {

        int startx = (int)(stopBoundary.x - (paddingX));
        int starty = (int)(stopBoundary.y - (paddingY));
        int width = (int)(stopBoundary.width + paddingX*2);
        int height = (int)(stopBoundary.height + paddingY*2);

        return new Rectangle(startx, starty, width, height);

    }


    private static void getEStopBounds(Rectangle... stop) {

        for(int i = 0; i < stop.length; i++) {

            Rectangle supastop = expandedStopBoundary(stop[i], paddingX, paddingY);

            StopLeftBounds.add((double)supastop.x);
            StopTopBounds.add((double)supastop.y);
            StopRightBounds.add((double)(supastop.x + supastop.width));
            StopBottomBounds.add((double)(supastop.y + supastop.height));

        }
    }


    private static Rectangle robot() {

        return new Rectangle(
                (int)robotStartPosX,
                (int)robotStartPosY,
                (int)robotWidth,
                (int)robotLength
        );

    }






    private static void determinePathFind(double robotX, double robotY, double targetX, double targetY) {

        double centerX = robotX + (robotWidth / 2);
        double centerY = robotY + (robotLength / 2);

        double TcenterX = targetX + (targetWidth / 2);
        double TcenterY = targetY + (targetLength / 2);

        double closestLeftDistance = Double.MAX_VALUE;
        double closestRightDistance = Double.MAX_VALUE;
        double closestTopDistance = Double.MAX_VALUE;
        double closestBottomDistance = Double.MAX_VALUE;

        int closestLeftBoundary = -1;
        int closestRightBoundary = -1;
        int closestTopBoundary = -1;
        int closestBottomBoundary = -1;

        int closestBoundary = -1;
        double closestDistance = Double.MAX_VALUE;
        String closestSide = "";

        // Vertical path
        if (centerX == TcenterX) {

            // Target is ABOVE the robot
            if (TcenterY < centerY) {

                for (int i = 0; i < StopLeftBounds.size(); i++) {

                    if (centerX >= StopLeftBounds.get(i) &&
                            centerX <= StopRightBounds.get(i)) {

                        double tempDistance = Math.abs(
                                centerY - StopBottomBounds.get(i)
                        );

                        if (tempDistance < closestBottomDistance) {
                            closestBottomDistance = tempDistance;
                            closestBottomBoundary = i;
                        }

                    }

                }

            }

            // Target is BELOW the robot
            else if (TcenterY > centerY) {

                for (int i = 0; i < StopLeftBounds.size(); i++) {

                    if (centerX >= StopLeftBounds.get(i) &&
                            centerX <= StopRightBounds.get(i)) {

                        double tempDistance = Math.abs(
                                StopTopBounds.get(i) - centerY
                        );

                        if (tempDistance < closestTopDistance) {
                            closestTopDistance = tempDistance;
                            closestTopBoundary = i;
                        }

                    }

                }

            }

            // Same center = already at target
            else {

                System.out.println("Robot is already at target.");

            }

        }

        else {

            double slope =
                    (centerY - TcenterY) /
                            (centerX - TcenterX);

            double intercept = centerY - slope * centerX;

            // LEFT EDGE
            for (int i = 0; i < StopLeftBounds.size(); i++) {

                double yAtLeft =
                        slope * StopLeftBounds.get(i) + intercept;

                if (yAtLeft >= StopTopBounds.get(i) &&
                        yAtLeft <= StopBottomBounds.get(i)) {

                    double tempDistance = Math.hypot(
                            StopLeftBounds.get(i) - centerX,
                            yAtLeft - centerY
                    );

                    if (tempDistance < closestLeftDistance) {
                        closestLeftDistance = tempDistance;
                        closestLeftBoundary = i;
                    }

                }

            }

            // RIGHT EDGE
            for (int i = 0; i < StopRightBounds.size(); i++) {

                double yAtRight =
                        slope * StopRightBounds.get(i) + intercept;

                if (yAtRight >= StopTopBounds.get(i) &&
                        yAtRight <= StopBottomBounds.get(i)) {

                    double tempDistance = Math.hypot(
                            StopRightBounds.get(i) - centerX,
                            yAtRight - centerY
                    );

                    if (tempDistance < closestRightDistance) {
                        closestRightDistance = tempDistance;
                        closestRightBoundary = i;
                    }

                }

            }

            // TOP EDGE
            if (slope != 0) {

                for (int i = 0; i < StopLeftBounds.size(); i++) {

                    double xAtTop =
                            (StopTopBounds.get(i) - intercept) / slope;

                    if (xAtTop >= StopLeftBounds.get(i) &&
                            xAtTop <= StopRightBounds.get(i)) {

                        double tempDistance = Math.hypot(
                                xAtTop - centerX,
                                StopTopBounds.get(i) - centerY
                        );

                        if (tempDistance < closestTopDistance) {
                            closestTopDistance = tempDistance;
                            closestTopBoundary = i;
                        }

                    }

                }

                // BOTTOM EDGE
                for (int i = 0; i < StopLeftBounds.size(); i++) {

                    double xAtBottom =
                            (StopBottomBounds.get(i) - intercept) / slope;

                    if (xAtBottom >= StopLeftBounds.get(i) &&
                            xAtBottom <= StopRightBounds.get(i)) {

                        double tempDistance = Math.hypot(
                                xAtBottom - centerX,
                                StopBottomBounds.get(i) - centerY
                        );

                        if (tempDistance < closestBottomDistance) {
                            closestBottomDistance = tempDistance;
                            closestBottomBoundary = i;
                        }

                    }

                }

            }

        }

        // FIND THE CLOSEST VALID INTERSECTION

        if (closestLeftDistance < closestDistance) {

            closestDistance = closestLeftDistance;
            closestBoundary = closestLeftBoundary;
            closestSide = "left";

        }

        if (closestRightDistance < closestDistance) {

            closestDistance = closestRightDistance;
            closestBoundary = closestRightBoundary;
            closestSide = "right";

        }

        if (closestTopDistance < closestDistance) {

            closestDistance = closestTopDistance;
            closestBoundary = closestTopBoundary;
            closestSide = "top";

        }

        if (closestBottomDistance < closestDistance) {

            closestDistance = closestBottomDistance;
            closestBoundary = closestBottomBoundary;
            closestSide = "bottom";

        }

        // REPORT RESULT

        if (closestBoundary != -1) {

            double distanceToBarrier = closestDistance;

            System.out.println(
                    "Intersects on the " +
                            closestSide +
                            " side of boundary " +
                            closestBoundary +
                            " in approximately " +
                            distanceToBarrier +
                            " in."
            );

        } else {

            System.out.println("No boundary intersection.");
            System.out.println("Straight path ahead.");

        }

    }







    //This is required for Dijkstra's algorithm, and everything here and below is for it. Don't ask too much about what it is. It is just pathfinding with nodes pretty much.

    static class Node {

        double x;
        double y;

        ArrayList<Edge> edges = new ArrayList<>();

        Node(double x, double y) {

            this.x = x;
            this.y = y;

        }

    }

    static class Edge {//Also calculates the edges, allowing us to retrieve distance between each node.

        Node destination;
        double distance;
        double heading;

        Edge(Node destination, double distance, double heading) {

            this.destination = destination;
            this.distance = distance;
            this.heading = heading;

        }

    }





    //This method creates another rectangle that creates an obstacle rectangle, checks if a line between two nodes intersects it, and returns true if it does.
    //This code will be used to stop inaccessible nodes from being connected.

    private static boolean pathBlocked(Node a, Node b) {

        for (int i = 0; i < StopLeftBounds.size(); i++) {

            double left = StopLeftBounds.get(i);
            double top = StopTopBounds.get(i);
            double right = StopRightBounds.get(i);
            double bottom = StopBottomBounds.get(i);

            if (lineIntersectsRectangle(
                    a.x, a.y,
                    b.x, b.y,
                    left, top,
                    right, bottom
            )) {
                return true;
            }
        }

        return false;
    }

    //Builds a "graph" of each node, allowing us to retrieve all edges and get distance between each node.

    private static void buildGraph() {

        for (int i = 0; i < nodes.size(); i++) {

            for (int j = i + 1; j < nodes.size(); j++) {

                Node a = nodes.get(i);
                Node b = nodes.get(j);

                // If the robot can travel directly between these nodes
                if (!pathBlocked(a, b)) {

                    double distance = Math.hypot(b.x - a.x, b.y - a.y);

                    double heading = Math.atan2(b.y - a.y, b.x - a.x);
                    double reverseHeading = Math.atan2(a.y - b.y, a.x - b.x);

                    // Create connections in BOTH directions
                    a.edges.add(new Edge(b, distance, heading));
                    b.edges.add(new Edge(a, distance, reverseHeading));

                }

            }

        }

    }

    //Good for debugging. Prints out each node coord.

    private static void printGraph() {

        for (int i = 0; i < nodes.size(); i++) {

            Node node = nodes.get(i);

            System.out.println("Node " + i + ":");

            for (Edge edge : node.edges) {

                int destinationIndex = nodes.indexOf(edge.destination);

                System.out.println(
                        "    -> Node " +
                                destinationIndex +
                                " : " +
                                edge.distance + ", heading: " + edge.heading + " degrees."
                );

            }

        }

    }

    //We are officially starting the Dijkstra algorithm. Please don't ask me what anything below does, as this isn't my algorithm... >:(

    static class NodeDistance implements Comparable<NodeDistance> {

        Node node;
        double distance;

        NodeDistance(Node node, double distance) {

            this.node = node;
            this.distance = distance;

        }

        @Override
        public int compareTo(NodeDistance other) {

            return Double.compare(this.distance, other.distance);

        }

    }

    //Especially don't ask me what this stuff does. Looks like hieroglyphics in coding to me tbh.

    private static ArrayList<Node> dijkstra(Node start, Node target) {

        Map<Node, Double> distances = new HashMap<>();

        Map<Node, Node> previous = new HashMap<>();

        PriorityQueue<NodeDistance> queue =
                new PriorityQueue<>();

        Set<Node> visited = new HashSet<>();

        // Start by giving every node an infinite distance
        for (Node node : nodes) {

            distances.put(node, Double.MAX_VALUE);

        }

        // The distance from the start to itself is 0
        distances.put(start, 0.0);

        queue.add(new NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {

            NodeDistance currentEntry = queue.poll();

            Node current = currentEntry.node;

            // Ignore this node if we've already processed it
            if (visited.contains(current)) {

                continue;

            }

            visited.add(current);

            // We reached the target
            if (current == target) {

                break;

            }

            // Check every connection from the current node
            for (Edge edge : current.edges) {

                Node neighbor = edge.destination;

                if (visited.contains(neighbor)) {

                    continue;

                }

                double newDistance =
                        distances.get(current) + edge.distance;

                // Did we find a shorter route?
                if (newDistance < distances.get(neighbor)) {

                    distances.put(neighbor, newDistance);

                    previous.put(neighbor, current);

                    queue.add(
                            new NodeDistance(neighbor, newDistance)
                    );

                }

            }

        }

        // Reconstruct the path
        ArrayList<Node> path = new ArrayList<>();

        Node current = target;

        while (current != null) {

            path.add(0, current);

            current = previous.get(current);

        }

        return path;

    }
    private static Edge findEdge(Node from, Node to) {

        for (Edge edge : from.edges) {

            if (edge.destination == to) {
                return edge;
            }

        }

        return null;
    }
    public static String getPathDebug() {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < shortestPath.size(); i++) {

            Node node = shortestPath.get(i);

            result.append("Node ")
                    .append(i)
                    .append(": (")
                    .append(node.x)
                    .append(", ")
                    .append(node.y)
                    .append(")\n");
        }

        return result.toString();
    }

    public static void drawMap(Canvas c) {
        // Draw obstacle safety boundaries (Yellow) - shifted by -72 to center on FTC Dashboard
        c.setStroke("#FFD700");
        c.setStrokeWidth(1);
        for (int i = 0; i < StopLeftBounds.size(); i++) {
            double left = StopLeftBounds.get(i) - 72;
            double top = StopTopBounds.get(i) - 72;
            double right = StopRightBounds.get(i) - 72;
            double bottom = StopBottomBounds.get(i) - 72;
            c.strokeRect(left, top, right - left, bottom - top);
        }

        // Draw graph nodes (Blue)
        c.setStroke("#0000FF");
        for (Node node : nodes) {
            c.strokeCircle(node.x - 72, node.y - 72, 1);
        }

        // Draw Dijkstra's calculated path (Green)
        c.setStroke("#00FF00");
        c.setStrokeWidth(2);
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Node a = shortestPath.get(i);
            Node b = shortestPath.get(i + 1);
            c.strokeLine(a.x - 72, a.y - 72, b.x - 72, b.y - 72);
        }
    }
}
class Rectangle {
    double x;
    double y;
    double width;
    double height;

    Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}