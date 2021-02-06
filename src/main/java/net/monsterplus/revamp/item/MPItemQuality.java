package net.monsterplus.revamp.item;

public enum MPItemQuality
{
    TRASH(1),
    COMMON(2),
    UNCOMMON(3),
    RARE(4),
    EPIC(5),
    LEGENDARY(6),
    UNKNOWN(7);

    private int value;

    private MPItemQuality(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
