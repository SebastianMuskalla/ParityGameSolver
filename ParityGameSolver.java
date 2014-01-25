import java.util.HashSet;

// This source code is available online
// https://github.com/SebastianMuskalla/ParityGameSolver

/**
 * Quick & Dirty Implementation of the McNaughton-Zielonka Parity Game Solver.
 * 
 * To keep the source code small, the coding style is very bad (as you can see).
 * 
 * @author Sebastian Muskalla
 * 
 */
public class ParityGameSolver
{
    public static void main (String[] args)
    {
        {
            System.out.println("Example 1\n\n");

            ParityGame first = new ParityGame();

            Position a = new Position("a", Player.A, 2);
            Position b = new Position("b", Player.P, 2);
            Position c = new Position("c", Player.A, 1);
            Position d = new Position("d", Player.P, 0);
            Position e = new Position("e", Player.A, 1);
            Position f = new Position("f", Player.P, 0);
            Position g = new Position("g", Player.A, 0);
            Position h = new Position("h", Player.A, 0);

            first.addPos(a);
            first.addPos(b);
            first.addPos(c);
            first.addPos(d);
            first.addPos(e);
            first.addPos(f);
            first.addPos(g);
            first.addPos(h);

            first.addTrans(new Transition(a, b));
            first.addTrans(new Transition(a, h));
            first.addTrans(new Transition(b, c));
            first.addTrans(new Transition(b, d));
            first.addTrans(new Transition(c, b));
            first.addTrans(new Transition(d, c));
            first.addTrans(new Transition(d, f));
            first.addTrans(new Transition(e, d));
            first.addTrans(new Transition(f, e));
            first.addTrans(new Transition(f, h));
            first.addTrans(new Transition(g, f));
            first.addTrans(new Transition(g, h));
            first.addTrans(new Transition(h, g));
            first.addTrans(new Transition(h, a));

            System.out.println(first.MCNZSolve().toString());

            /*
             * Example 1
             * 
             * 
             * Winning region of A
             * h
             * g
             * a
             * 
             * Winning region of P
             * e
             * c
             * f
             * b
             * d
             */
        }

        {
            System.out.println("Example 2\n\n");

            ParityGame second = new ParityGame();

            Position aA = new Position("a'", Player.A, 0);
            Position bA = new Position("b'", Player.A, 1);
            Position cA = new Position("c'", Player.A, 2);
            Position dA = new Position("d'", Player.A, 3);
            Position eA = new Position("e'", Player.A, 4);
            Position fA = new Position("f'", Player.A, 5);
            Position gA = new Position("g'", Player.A, 6);
            Position hA = new Position("h'", Player.A, 7);

            Position aP = new Position("a", Player.P, 0);
            Position bP = new Position("b", Player.P, 1);
            Position cP = new Position("c", Player.P, 2);
            Position dP = new Position("d", Player.P, 3);
            Position eP = new Position("e", Player.P, 4);
            Position fP = new Position("f", Player.P, 5);
            Position gP = new Position("g", Player.P, 6);
            Position hP = new Position("h", Player.P, 7);

            second.addPos(aA);
            second.addPos(bA);
            second.addPos(cA);
            second.addPos(dA);
            second.addPos(eA);
            second.addPos(fA);
            second.addPos(gA);
            second.addPos(hA);

            second.addPos(aP);
            second.addPos(bP);
            second.addPos(cP);
            second.addPos(dP);
            second.addPos(eP);
            second.addPos(fP);
            second.addPos(gP);
            second.addPos(hP);

            second.addTrans(new Transition(aA, aP));
            second.addTrans(new Transition(aP, aA));

            second.addTrans(new Transition(bA, bP));
            second.addTrans(new Transition(bP, bA));

            second.addTrans(new Transition(cA, cP));
            second.addTrans(new Transition(cP, cA));

            second.addTrans(new Transition(dA, dP));
            second.addTrans(new Transition(dP, dA));

            second.addTrans(new Transition(eA, eP));
            second.addTrans(new Transition(eP, eA));

            second.addTrans(new Transition(fA, fP));
            second.addTrans(new Transition(fP, fA));

            second.addTrans(new Transition(gA, gP));
            second.addTrans(new Transition(gP, gA));

            second.addTrans(new Transition(hA, hP));
            second.addTrans(new Transition(hP, hA));

            second.addTrans(new Transition(aA, bP));

            second.addTrans(new Transition(bA, cP));
            second.addTrans(new Transition(bP, cA));

            second.addTrans(new Transition(cP, dA));

            second.addTrans(new Transition(dA, eP));
            second.addTrans(new Transition(dP, eA));

            second.addTrans(new Transition(eA, fP));

            second.addTrans(new Transition(fA, gP));
            second.addTrans(new Transition(fP, gA));

            second.addTrans(new Transition(fP, hA));

            second.addTrans(new Transition(cA, hP));
            second.addTrans(new Transition(gA, dP));
            second.addTrans(new Transition(hP, aP));
            second.addTrans(new Transition(eP, bA));
            second.addTrans(new Transition(aP, fA));

            System.out.println(second.MCNZSolve().toString());
            /*
             * Example 2
             * 
             * 
             * Winning region of A
             * e
             * c
             * e'
             * g
             * a'
             * c'
             * d
             * b
             * f'
             * a
             * g'
             * b'
             * d'
             * 
             * Winning region of P
             * f
             * h'
             * h
             */
        }

    }
}

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

class Transition
{
    Position from;
    Position to;

    public Transition (Position from, Position to)
    {
        this.from = from;
        this.to = to;
    }
}

enum Player
{
    A, P;
}

class ParityGame
{
    // We use HashSet since they are clonable and Java guarantees that we don't add duplicates
    HashSet<Position> positions;
    HashSet<Transition> transitions;

    public ParityGame ()
    {
        positions = new HashSet<Position>();
        transitions = new HashSet<Transition>();
    }

    public void addTrans (Transition transition)
    {
        transitions.add(transition);
    }

    public void addPos (Position position)
    {
        positions.add(position);
    }

    /*************************************************
     * SOLVER
     ************************************************/

    public int getMaxPriority ()
    {
        int n = -1;
        for (Position p : positions)
        {
            if (p.priority > n)
            {
                n = p.priority;
            }
        }
        return n;
    }

    public HashSet<Position> getPositionsWithPriority (int n)
    {
        HashSet<Position> res = new HashSet<Position>();

        for (Position p : positions)
        {
            if (p.priority == n)
            {
                res.add(p);
            }
        }
        return res;
    }

    public TupleOfWinningRegions MCNZSolve ()
    {
        TupleOfWinningRegions res = new TupleOfWinningRegions();

        if (positions.isEmpty())
        {
            return res;
        }

        int n = getMaxPriority();

        if (n < 0)
        {
            throw new RuntimeException("And then suddenly, something went terribly wrong...");
        }

        if (n == 0)
        {
            res.winningRegionOfA = positions;
            return res;
        }

        HashSet<Position> N = getPositionsWithPriority(n);

        HashSet<Position> attractor;

        // Compute the attractor of the positions with the highest priority
        if (isEven(n))
        {
            // n is even - A wins on the attractor
            attractor = attractorOf(Player.A, N);
        }
        else
        {
            // n is odd - P wins on the attractor
            attractor = attractorOf(Player.P, N);
        }

        // Shallow copy
        @SuppressWarnings("unchecked")
        HashSet<Position> complement = (HashSet<Position>) positions.clone();
        complement.removeAll(attractor);

        // Compute the subgame induced by the complement of the attractor
        ParityGame subgame = subgameInducedBy(complement);
        TupleOfWinningRegions wAwB = subgame.MCNZSolve();

        // We check if the "other player" can't win the subgame on the complement
        // Then the "player" wins the whole game
        if (isEven(n) && wAwB.winningRegionOfP.isEmpty())
        {
            res.winningRegionOfA = positions;
            return res;
        }
        if (!isEven(n) && wAwB.winningRegionOfA.isEmpty())
        {
            res.winningRegionOfP = positions;
            return res;
        }

        // The other player wins at least a part of the subgame
        // We have to calculate on which positions he can enforce a visit to his winning region

        HashSet<Position> attractor2;

        if (isEven(n))
        {
            attractor2 = attractorOf(Player.P, wAwB.winningRegionOfP);
        }
        else
        {
            attractor2 = attractorOf(Player.A, wAwB.winningRegionOfA);
        }

        // Shallow copy
        @SuppressWarnings("unchecked")
        // This cast will never fail
        HashSet<Position> complement2 = (HashSet<Position>) positions.clone();
        complement2.removeAll(attractor2);

        // Compute the subgame induced by the complement of the attractor
        ParityGame subgame2 = subgameInducedBy(complement2);
        TupleOfWinningRegions wAwB2 = subgame2.MCNZSolve();

        res.winningRegionOfA = wAwB2.winningRegionOfA;
        res.winningRegionOfP = wAwB2.winningRegionOfP;

        // Now add the corresponding part from the subgame
        if (isEven(n))
        {
            res.winningRegionOfP.addAll(attractor2);

        }
        else
        {
            res.winningRegionOfA.addAll(attractor2);
        }

        return res;
    }

    /*************************************************
     * COMPUTE ATTRACTOR
     ************************************************/

    public HashSet<Position> attractorOf (Player player, HashSet<Position> N)
    {
        HashSet<Position> attractor = N;

        // This is only a shallow copy - the Position objects are the same and can be compared using "=="
        @SuppressWarnings("unchecked")
        HashSet<Position> complement_of_attractor = (HashSet<Position>) positions.clone();
        complement_of_attractor.removeAll(N);

        outerLoop:
        while (true)
        {

            for (Position p : complement_of_attractor)
            {
                // We check, if the position belongs to the player and the player has a choice to go to the attractor
                // or if it belongs to the other player and the other player has no choice but to go the attractor
                if ((p.player == player && canGoFromTo(p, attractor))
                        || (p.player != player && mustGoFromTo(p, attractor)))
                {
                    attractor.add(p);
                    complement_of_attractor.remove(p);

                    // We will get nasty errors if we continue the loop since we just removed an element of it
                    // (which may be the next element in the iteration - autsch!)
                    // So we will jump to the start of the outer loop again
                    // This will cause the loop to run until the attractor doesn't change anymore
                    continue outerLoop;
                }
            }
            return attractor;

        }
    }

    boolean canGoFromTo (Position p, HashSet<Position> attractor)
    {
        for (Transition t : transitions)
        {
            if (t.from == p && attractor.contains(t.to))
            {
                return true;
            }
        }
        return false;
    }

    boolean mustGoFromTo (Position p, HashSet<Position> attractor)
    {
        for (Transition t : transitions)
        {
            if (t.from == p && !attractor.contains(t.to))
            {
                return false;
            }
        }

        // Small Sanity check
        // If we dont have any transition NOT into the attractor
        // we should have a transition in the attractor since the game doesn't deadlock
        if (!canGoFromTo(p, attractor))
        {
            throw new RuntimeException("Game can Deadlock - something went wrong");
        }

        return true;
    }

    /*************************************************
     * COMPUTE SUBGAME
     ************************************************/

    /**
     * The subgame is a partial shallow copy of the nodes and transitions induced by a set of nodes
     * The nodes and transitions are still the same objects as in the original game.
     * The method doesn't check if the subgame deadlocks. This should never happen by construction.
     */
    public ParityGame subgameInducedBy (HashSet<Position> N)
    {
        ParityGame subgame = new ParityGame();
        subgame.positions = N;

        for (Transition t : transitions)
        {
            if (N.contains(t.from) && N.contains(t.to))
            {
                subgame.transitions.add(t);
            }
        }
        return subgame;
    }

    /*************************************************
     * OTHER HELPER STUFF
     ************************************************/

    static boolean isEven (int n)
    {
        return (n % 2) == 0;
    }

}

class TupleOfWinningRegions
{
    HashSet<Position> winningRegionOfA;
    HashSet<Position> winningRegionOfP;

    public TupleOfWinningRegions ()
    {
        winningRegionOfA = new HashSet<Position>();
        winningRegionOfP = new HashSet<Position>();
    }

    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Winning region of A\n");
        for (Position p : winningRegionOfA)
        {
            sb.append(p.name + "\n");
        }
        sb.append("\n");

        sb.append("Winning region of P\n");
        for (Position p : winningRegionOfP)
        {
            sb.append(p.name + "\n");
        }
        sb.append("\n\n");

        return sb.toString();
    }
}
