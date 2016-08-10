package me.gamerbah.Utils;/* Created by GamerBah on 6/17/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;

@AllArgsConstructor
@Getter
public enum EventSound {

    COMMAND_SUCCESS(Sound.BLOCK_NOTE_HARP, 1, 2F, null, 0, 0),
    COMMAND_FAIL(Sound.ITEM_FLINTANDSTEEL_USE, 1, 1, null, 0, 0),
    COMMAND_CLICK(Sound.BLOCK_COMPARATOR_CLICK, 0.5F, 1.3F, null, 0, 0),
    COMMAND_NEEDS_CONFIRMATION(Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1, null, 0, 0);

    private Sound sound1;
    private float vol1;
    private float ptch1;
    private Sound sound2;
    private float vol2;
    private float ptch2;
}
