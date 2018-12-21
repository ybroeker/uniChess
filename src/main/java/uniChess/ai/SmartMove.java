package uniChess.ai;

import java.util.Comparator;

import uniChess.Game;
import uniChess.Location;
import uniChess.Move;

class SmartMove extends Move implements Comparable<SmartMove> {
    public static int MATERIAL_WEIGHT = 1;

    public double[] unWeightedTreeAverages;


    public double strategicValue = 0;

    public double tacticalValue = 0.0;

    public double locationValue;

    public SmartMove(Move move) {
        super(move);

        this.materialValue *= MATERIAL_WEIGHT;
        this.tacticalValue = this.materialValue;
        this.locationValue = (getDistanceToTarget(this.origin) - getDistanceToTarget(this.destination));

    }

    /**
     * returns absolute distance from target location (opposite king for normal pieces, default king position for king pieces)
     */
    public double getDistanceToTarget(Location location) {
        if (!this.movingPiece.type.equals(Game.PieceType.KING)) {
            return this.board.getDistanceFromKing(Game.getOpposite(this.movingPiece.color), location);
        } else {
            return this.board.getDistanceFromLocation(location, (this.movingPiece.color.equals(Game.Color.WHITE) ? new Location(4, 0) : new Location(4, 7)));
        }
    }

    public double calculateStrategicValue() {
        double best = 0, worst = 0;
        for (double val : unWeightedTreeAverages) {
            if (val < worst) {
                worst = val;
            } else if (val > best) {
                best = val;
            }
        }
        return best + worst;
    }

    public double calculateStrategicValue(Chesster.StrategyType stype) {
        double sv = 0;
        //unWeightedTreeAverages[0] = this.tacticalValue;
        for (int i = 0; i < unWeightedTreeAverages.length; ++i) {

            double weightedVal = 0;

            switch (stype) {
                case LOG:
                    weightedVal = unWeightedTreeAverages[i] * (1.0 / Math.log(i + Math.E));
                    break;

                case LINEAR:
                    weightedVal =
                            unWeightedTreeAverages[i] * ((-(1.0 / (unWeightedTreeAverages.length + 1)) * (double) i))
                            + 1.0;
                    break;

                case EXP2:
                    weightedVal = unWeightedTreeAverages[i] * (1.0 / Math.pow(2.0, i));
                    break;

                case EXP4:
                    weightedVal = unWeightedTreeAverages[i] * (1.0 / Math.pow(4.0, i));
                    break;

                case EXP10:
                    weightedVal = unWeightedTreeAverages[i] * (1.0 / Math.pow(10.0, i));
                    break;
            }

            sv += weightedVal;
        }
        return sv;
    }

    /**
     * Compares Moves based on strategic/tactical value, then on location value if strategic/tactical value is equivalent.
     */
    @Override
    public int compareTo(SmartMove other) {
        return Comparator.comparing(SmartMove::isCheckmate)
                .thenComparing(SmartMove::getStrategicValue)
                .thenComparing(SmartMove::getMaterialValue)
                .thenComparing(SmartMove::getLocationValue)
                .compare(this, other);
    }

    public String getDataSring() {
        String res = "";

        res += String.format("%s := %.2f :: ", this.toString(), strategicValue);
        int depth = 0;
        for (double d : unWeightedTreeAverages) {
            res += String.format("n[%s]: %.2f | ", (depth++), d);
        }
        return res;
    }

    public double getStrategicValue() {
        return strategicValue;
    }

    public double getTacticalValue() {
        return tacticalValue;
    }

    public double getLocationValue() {
        return locationValue;
    }
}
