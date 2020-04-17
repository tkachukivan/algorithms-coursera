import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private final int teamsNumber;
    private final HashMap<String, Integer> teams = new HashMap<>();
    private final HashMap<String, HashSet<String>> eliminations = new HashMap<>();
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] games;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamsNumber = in.readInt();
        wins = new int[teamsNumber];
        losses = new int[teamsNumber];
        remaining = new int[teamsNumber];
        games = new int[teamsNumber][teamsNumber];

        for (int i = 0; i < teamsNumber; i++) {
            teams.put(in.readString(), i);

            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < teamsNumber; j++) {
                games[i][j] = in.readInt();
            }
        }

        calculateEliminations();
    }

    public int numberOfTeams() {
        return teamsNumber;
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int wins(String team) {
        checkTeamValidity(team);

        return wins[teams.get(team)];
    }

    public int losses(String team) {
        checkTeamValidity(team);

        return losses[teams.get(team)];
    }

    public int remaining(String team) {
        checkTeamValidity(team);

        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        checkTeamValidity(team1);
        checkTeamValidity(team2);

        return games[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team) {
        checkTeamValidity(team);
        return eliminations.containsKey(team);
    }

    public Iterable<String> certificateOfElimination(String team) {
        checkTeamValidity(team);
        if (eliminations.containsKey(team)) {
            return eliminations.get(team);
        } else {
            return null;
        }
    }

    private void checkTeamValidity(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException();
        }
    }

    private void calculateEliminations() {
        for (String team: teams.keySet()) {
            int teamIndex = teams.get(team);

            // check maxNumber of games team can win up to the end of season
            int maxWins = wins[teamIndex] + remaining[teamIndex];
            for (int i = 0; i < teamsNumber; i++) {
                if (i == teamIndex) continue;
                if (maxWins < wins[i]) {
                    HashSet<String> cert = new HashSet<>();
                    cert.add(getTeamNameByIndex(i));
                    eliminations.put(team, cert);
                }
            }
            // team is eliminated, no reason to continue
            if (eliminations.containsKey(team)) continue;

            int gameVertices = ((teamsNumber - 2) * (teamsNumber - 1) / 2);  // number of games vertices
            int flowNetworkSize = gameVertices + 2 + teamsNumber;            // size of flowNetwok is equal to number of teams + number of games + source and target vertices

            FlowNetwork flowNetwork = new FlowNetwork(flowNetworkSize);


            int currentGameVertice = 1;                // game vertices starts from 1
            int baseTeamVertice = gameVertices + 1;    // teams verices start from number of game vertices + 1(source)
            // add edges from source to game and from game to team
            for (int i = 0; i < teamsNumber; i++) {
                if (i == teamIndex) continue;

                for (int j = i + 1; j < teamsNumber; j++) {
                    if (j == teamIndex) continue;

                    int v = 0;
                    double capacity = games[i][j];
                    FlowEdge edgeG = new FlowEdge(v, currentGameVertice, capacity);
                    flowNetwork.addEdge(edgeG);

                    FlowEdge edgeT1 = new FlowEdge(currentGameVertice, baseTeamVertice + i,
                                                   Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(edgeT1);

                    FlowEdge edgeT2 = new FlowEdge(currentGameVertice, baseTeamVertice + j,
                                                   Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(edgeT2);

                    currentGameVertice++;
                }
            }

            // add edges from team to target
            for (int i = 0; i < teamsNumber; i++) {
                if (i == teamIndex) continue;

                double capacity = wins[teamIndex] + remaining[teamIndex] - wins[i];
                FlowEdge edge = new FlowEdge(baseTeamVertice + i, flowNetworkSize - 1, capacity);
                flowNetwork.addEdge(edge);
            }

            FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetworkSize - 1);

            for (FlowEdge flowEdge: flowNetwork.adj(0)) {

                // If some edges pointing from source are not full, then there is no scenario in which team can win the division
                // then we need to collect certificates of elimination
                if (Double.compare(flowEdge.flow(), flowEdge.capacity()) != 0) {
                    HashSet<String> cert = new HashSet<>();
                    for (FlowEdge edge: flowNetwork.adj(0)) {
                        // if inCut add certificate of elimination
                        if (fordFulkerson.inCut(edge.to())) {
                            for (FlowEdge edgeToTeam: flowNetwork.adj(edge.to())) {
                                String teamName = getTeamNameByIndex(edgeToTeam.to() - baseTeamVertice);
                                if (teamName != null) {
                                    cert.add(teamName);
                                }
                            }
                        }
                    }

                    eliminations.put(team, cert);
                    break;
                }
            }
        }
    }

    private String getTeamNameByIndex(int teamIndex) {
        for (String team: teams.keySet()) {
            if (teams.get(team) == teamIndex) {
                return team;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams4.txt");

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
