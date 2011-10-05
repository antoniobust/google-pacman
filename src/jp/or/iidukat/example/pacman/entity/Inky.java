package jp.or.iidukat.example.pacman.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import jp.or.iidukat.example.pacman.Direction;
import jp.or.iidukat.example.pacman.GhostMode;
import jp.or.iidukat.example.pacman.Move;
import jp.or.iidukat.example.pacman.PacmanGame;

public class Inky extends Ghost {

    private static final InitPosition INIT_POS =
        InitPosition.createGhostInitPosition(37.625f, 7, Direction.UP, 57, 20);

    // モンスターの巣の中での動き
    private static final Map<GhostMode, MoveInPen[]> MOVES_IN_PEN;
    static {
        Map<GhostMode, MoveInPen[]> m =
            new EnumMap<GhostMode, MoveInPen[]>(GhostMode.class);
        m.put(
            GhostMode.IN_PEN,
            new MoveInPen[] {
                      new MoveInPen(37.6f, 7, Direction.UP, 6.375f, 0.48f),
                      new MoveInPen(37.6f, 6.375f, Direction.DOWN, 7.625f, 0.48f),
                      new MoveInPen(37.6f, 7.625f, Direction.UP, 7, 0.48f),
              });            
        m.put(
            GhostMode.EXITING_FROM_PEN,
            new MoveInPen[] {
                      new MoveInPen(37.6f, 7, Direction.RIGHT, 39.5f, y),
                      new MoveInPen(39.5f, 7, Direction.UP, 4, y),
              });            
        m.put(
            GhostMode.ENTERING_PEN,
            new MoveInPen[] {
                     new MoveInPen(39.5f, 4, Direction.DOWN, 7, 1.6f),
                     new MoveInPen(39.5f, 7, Direction.LEFT, 37.625f, 1.6f),
                 });            
        m.put(
            GhostMode.RE_EXITING_FROM_PEN,
            new MoveInPen[] {
                     new MoveInPen(37.6f, 7, Direction.RIGHT, 39.5f, y),
                     new MoveInPen(39.5f, 7, Direction.UP, 4, y),
                 });            
        MOVES_IN_PEN = Collections.unmodifiableMap(m);
    }

    public Inky(int b, PacmanGame g) {
        super(b, g);
    }
    
    @Override
    InitPosition getInitPosition() {
        return INIT_POS;
    }
    
    // ターゲットポジションを決定
    @Override
    public void B() {
        if (this.mode != GhostMode.CHASE) {
            return;
        }
        
        // PlayerをBLINKYと挟み撃ちにする
        Actor b = g.getPlayer();
        Move c = b.dir.getMove();
        Actor d = g.getGhosts()[0];
        float[] f = new float[] { b.tilePos[0], b.tilePos[1] };
        f[c.getAxis()] += 16 * c.getIncrement();
        if (b.dir == Direction.UP) f[1] -= 16;
        this.targetPos[0] = f[0] * 2 - d.tilePos[0];
        this.targetPos[1] = f[1] * 2 - d.tilePos[1];
    }
    
    @Override
    MoveInPen[] getMovesInPen() {
        if (MOVES_IN_PEN.containsKey(mode)) {
            return MOVES_IN_PEN.get(mode);
        } else {
            return new MoveInPen[0];
        }
    }
    
    @Override
    int getOrdinaryImageRow() {
        return 6; // 4 + this.id - 1;
    }    
}