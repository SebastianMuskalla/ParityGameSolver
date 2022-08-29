import java.util.*;
import java.util.stream.Collectors;

/**
 * A naive, quick, and dirty implementation of the McNaughton-Zielonka algorithm for solving parity games.
 * <p>
 * Taken from the publication
 * "Infinite games on finitely coloured graphs with applications to automata on infinite trees"
 * W. Zielonka
 * Volume 200 of Theoretical Computer Science, 1998, pages 135-183,
 *
 * @author Sebastian Muskalla
 */
public class ParityGames
{
    public static void main (String[] args)
    {
        System.out.println("Example 1\n");
        firstExample();

        System.out.println("\n\n\n");

        System.out.println("Example 2\n");
        secondExample();
    }

    public static void firstExample ()
    {
        ParityGame game = new ParityGame();

        Position a = new Position("a", Player.Existential, 2);
        Position b = new Position("b", Player.Universal, 2);
        Position c = new Position("c", Player.Existential, 1);
        Position d = new Position("d", Player.Universal, 0);
        Position e = new Position("e", Player.Existential, 1);
        Position f = new Position("f", Player.Universal, 0);
        Position g = new Position("g", Player.Existential, 0);
        Position h = new Position("h", Player.Existential, 0);

        game.addPositions(a, b, c, d, e, f, g, h);

        game.addTransition(a, b);
        game.addTransition(a, h);
        game.addTransition(b, c);
        game.addTransition(b, d);
        game.addTransition(c, b);
        game.addTransition(d, c);
        game.addTransition(d, f);
        game.addTransition(e, d);
        game.addTransition(f, e);
        game.addTransition(f, h);
        game.addTransition(g, f);
        game.addTransition(g, h);
        game.addTransition(h, g);
        game.addTransition(h, a);

        new ParityGameSolver(game).solve();
    }

    public static void secondExample ()
    {
        ParityGame game = new ParityGame();

        Position a = new Position("a", Player.Universal, 0);
        Position b = new Position("b", Player.Universal, 1);
        Position c = new Position("c", Player.Universal, 2);
        Position d = new Position("d", Player.Universal, 3);
        Position e = new Position("e", Player.Universal, 4);
        Position f = new Position("f", Player.Universal, 5);
        Position g = new Position("g", Player.Universal, 6);
        Position h = new Position("h", Player.Universal, 7);

        game.addPositions(a,b,c,d,e,f,g,h);

        Position i = new Position("i", Player.Existential, 0);
        Position j = new Position("j", Player.Existential, 1);
        Position k = new Position("k", Player.Existential, 2);
        Position l = new Position("l", Player.Existential, 3);
        Position m = new Position("m", Player.Existential, 4);
        Position n = new Position("n", Player.Existential, 5);
        Position o = new Position("o", Player.Existential, 6);
        Position p = new Position("p", Player.Existential, 7);

        game.addPositions(i,j,k,l,m,n,o,p);

        // Edge in both direction between x and x' for all x
        game.addTransition(i, a);
        game.addTransition(a, i);

        game.addTransition(j, b);
        game.addTransition(b, j);

        game.addTransition(k, c);
        game.addTransition(c, k);

        game.addTransition(l, d);
        game.addTransition(d, l);

        game.addTransition(m, e);
        game.addTransition(e, m);

        game.addTransition(n, f);
        game.addTransition(f, n);

        game.addTransition(o, g);
        game.addTransition(g, o);

        game.addTransition(p, h);
        game.addTransition(h, p);

        // other "vertical" edges
        game.addTransition(i, b);

        game.addTransition(j, c);
        game.addTransition(b, k);

        game.addTransition(c, l);

        game.addTransition(l, e);
        game.addTransition(d, m);

        game.addTransition(m, f);

        game.addTransition(n, g);
        game.addTransition(f, o);

        game.addTransition(g, p);

        // "horizontal" edges
        game.addTransition(k, h);
        game.addTransition(o, d);
        game.addTransition(h, a);
        game.addTransition(e, j);
        game.addTransition(a, n);

        new ParityGameSolver(game).solve();
    }
}

/**
 * a position in the parity game
 */
class Position
{
    String name;
    Player player;
    int priority;

    public Position (String name, Player player, int priority)
    {
        this.name = name;
        this.player = player;
        this.priority = priority;
    }
}


/**
 * the two players in the game
 */
enum Player
{
    Universal, Existential
}

/**
 * a class for immutable key-value pairs
 * @param <K> the type of the keys
 * @param <V> the type of the values
 */
class Pair<K,V>
{
    private final K key;
    private final V value;

    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    K getKey()
    {
        return key;
    }

    V getValue()
    {
        return value;
    }
}

/**
 * the game arena of a parity game
 */
class ParityGame
{
    Set<Position> positions;
    Set<Pair<Position, Position>> transitions;

    public ParityGame ()
    {
        positions = new HashSet<>();
        transitions = new HashSet<>();
    }

    public void addTransition (Position from, Position to)
    {
        transitions.add(new Pair<>(from, to));
    }

    public void addPosition (Position position)
    {
        positions.add(position);
    }

    public void addPositions (Position... multiplePositions)
    {
        positions.addAll(Arrays.asList(multiplePositions));
    }

    public int getMaxPriority ()
    {
        return positions.stream().mapToInt(p -> p.priority).max().orElseThrow();
    }

    public Set<Position> getPositionsWithPriority (int n)
    {
        return positions.stream().filter(p -> p.priority == n).collect(Collectors.toSet());
    }

    /**
     * compute the restriction of a parity game to a trap
     *
     * @param trap a set of positions that should be a trap. in particular, each position in the trap should have a successor in the trap. this is guaranteed if {@code trap} is the complement of an {@code attractor}
     * @return the restricted parity game
     */
    public ParityGame subgameInducedBy (Set<Position> trap)
    {
        ParityGame subgame = new ParityGame();
        subgame.positions = trap;
        subgame.transitions = transitions.stream().filter(t -> trap.contains(t.getKey()) && trap.contains(t.getValue())).collect(Collectors.toSet());
        return subgame;
    }
}

/**
 * the McNaughton-Zielonka solver
 */
class ParityGameSolver
{
    private int recursionDepth = 0;

    public static final boolean PRINT_TRACE= true;

    private final ParityGame parityGame;

    ParityGameSolver (ParityGame parityGame)
    {
        this.parityGame = parityGame;
    }

    private void trace (String message)
    {
        if (PRINT_TRACE)
        {
            System.out.println(String.join("", String.join("", Collections.nCopies(recursionDepth - 1, "|    ")) + message));
        }
    }

    private void trace (String message, Collection<Position> positions)
    {
        trace(message + ": {" + positions.stream().map(p -> p.name).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.joining(",")) + "}");
    }


    /**
     * Quick'n'dirty implementation of the algorithm to solve parity games on a deadlock-free game arena
     *
     * @return a map assigning each player their winning region as a non-null set
     */
    public Map<Player, Set<Position>> solve ()
    {
        return solve(parityGame);
    }

    private Map<Player, Set<Position>> solve (ParityGame game)
    {
        Map<Player, Set<Position>> result = new HashMap<>();
        result.put(Player.Universal, new HashSet<>());
        result.put(Player.Existential, new HashSet<>());

        recursionDepth++;

        if (game.positions.isEmpty())
        {
            trace("Solving game on empty game arena: {}");
        }
        else
        {
            trace("Solving game on game arena", game.positions);

            int n = game.getMaxPriority();

            trace("Highest priority is " + n);

            if (n == 0)
            {
                trace("Existential player wins on all positions");

                result.put(Player.Existential, game.positions);
            }
            else
            {
                Set<Position> positionsWithPriorityN = game.getPositionsWithPriority(n);

                trace("Positions N that have priority " + n, positionsWithPriorityN);

                Player player, opponent;

                if (n % 2 == 0)
                {
                    player = Player.Existential;
                    opponent = Player.Universal;
                }
                else
                {
                    player = Player.Universal;
                    opponent = Player.Existential;
                }

                Set<Position> attractorA = attractor(game, player, positionsWithPriorityN);

                trace("Attractor A of set N for the " + player + " Player", attractorA);

                Set<Position> complementOfAttractorA = new HashSet<>(game.positions);
                complementOfAttractorA.removeAll(attractorA);

                trace("Solving subgame on the complement of the attractor A");

                ParityGame subgameA = game.subgameInducedBy(complementOfAttractorA);

                Map<Player, Set<Position>> winningRegionsOfSubgameA = solve(subgameA);

                if (winningRegionsOfSubgameA.get(opponent).isEmpty())
                {
                    trace(player + " Player wins on all positions of the subgame");

                    result.put(player, game.positions);
                }
                else
                {
                    Set<Position> attractorB = attractor(game, opponent, winningRegionsOfSubgameA.get(opponent));
                    Set<Position> complementOfAttractorB = new HashSet<>(game.positions);
                    complementOfAttractorB.removeAll(attractorB);

                    trace(player + " Player wins NOT on all positions of the subgame");
                    trace("The winning region of the " + opponent + " Player is", winningRegionsOfSubgameA.get(opponent));
                    trace("Attractor B for the " + opponent + " Player of that set is", complementOfAttractorB);
                    trace("Solving subgame on the complement of the attractor B");

                    ParityGame subgameB = game.subgameInducedBy(complementOfAttractorB);

                    Map<Player, Set<Position>> winningRegionsOfSubgameB = solve(subgameB);

                    Set<Position> winningRegionOfOpponentInSubgameB = winningRegionsOfSubgameB.get(opponent);
                    winningRegionOfOpponentInSubgameB.addAll(attractorB);

                    trace("Winning region of the " + opponent + " Player is the winning region in the subgame plus the attractor");

                    result = winningRegionsOfSubgameB;
                    result.put(opponent, winningRegionOfOpponentInSubgameB);
                }
            } // if n == 0 else ...
        } // if empty else ....

        trace("* Winning region of the Existential Player", result.get(Player.Existential));
        trace("* Winning region of the Universal Player", result.get(Player.Universal));

        recursionDepth--;
        return result;
    }

    /**
     * naively compute the attractor for the target set {@code target} and the player {@code player}
     *
     * @param player the player
     * @param target the target set
     * @return the set of positions contained in the attractor
     */
    private Set<Position> attractor (ParityGame game, Player player, Set<Position> target)
    {
        Set<Position> attr = new HashSet<>(target);

        Set<Position> complementOfAttractor = new HashSet<>(game.positions);
        complementOfAttractor.removeAll(target);

        outerLoop:
        while (true)
        {
            for (Position p : complementOfAttractor)
            {
                if ((p.player == player && canGoFromPositionToSet(game, p, attr)) || (p.player != player && mustGoFromPositionToSet(game, p, attr)))
                {
                    attr.add(p);
                    complementOfAttractor.remove(p);
                    continue outerLoop;
                }
            }
            return attr;
        }
    }

    private boolean canGoFromPositionToSet (ParityGame game, Position p, Set<Position> target)
    {
        return game.transitions.stream().anyMatch(t -> t.getKey() == p && target.contains(t.getValue()));
    }

    private boolean mustGoFromPositionToSet (ParityGame game, Position p, Set<Position> target)
    {
        return game.transitions.stream().noneMatch(t -> t.getKey() == p && !target.contains(t.getValue()));
    }
}


