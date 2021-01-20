# Locksmithery

_Simple locksmithing-based security and safety for Minecraft_

This is a (presently 1.12.2) mod that adds semi-realistic locksmithing to
Minecraft. Specifically, the mod revolves around _keys_, which come in four
craftable flavors (wood, iron, gold, and diamond, in order of the number of
pins and settings), and _locksets_ which may fit one or more keys. Control over
which key fits which lockset is by their mutual bitting, which is configured
manually at a Locksmith Workbench. By putting multiple cuts into locksets, it
is possible to design master/change key systems for authority delegation, just
like in real life.

## Locksmithing

To start, craft either a lockset or a key, and place it in a Locksmith
Workbench (which, as of this writing, is crafted with a wood key on top of a
crafting table--the uncut wood key is consumed in that recipe). You'll see an
interface like the following (apologies for its messiness):

![bitting key wood](../doc/img/bitting_key_wood.png)

The columns each represent an individual _pin_ (sometimes a _position_) that
the key will interface with. The numbers across the rows represent a _depth of
cut_, or just a _cut_. Keys can only be _bitted_ (cut in their entirety) to one
depth; once you've chosen one cut for each pin, you can extract the key from
the bottom slot. The tooltip for the key always reveals its bitting.

![bitting key diamond](../doc/img/bitting_key_diamond.png)

Here is an example of a diamond key, with many more pins and _settings_ (depths
of cut). Despite many more possible configurations and a larger interface, the
functionality is the same. Note that a key won't fit a different tier of
lockset, so this key can only be used with diamond locksets, and the former
only with wood locksets.

![bitting lockset diamond](../doc/img/bitting_lockset_diamond.png)

The locksets are configured similarly, but have one notable difference: their
pins can have _multiple_ cuts. A key _fits_ a lockset if, for _every_ position,
the key's depth of cut matches _one_ of the lockset's cuts. The example here
shows a lockset with mostly only one cut per pin, but the last two pins have
two cuts, so this will fit four different (diamond) keys:

- [4, 3, 3, 4, 1, 2, 2, 1],
- [4, 3, 3, 4, 1, 2, 6, 1],
- [4, 3, 3, 4, 1, 2, 2, 5], and
- [4, 3, 3, 4, 1, 2, 6, 5].

It is, of course, up to you which of these keys you will use, cut, and
allocate. It is typical to leave one of these configurations as a "master"
configuration set across many locksets, and modify some other cuts to make the
"change" (non-master) keys.

## Keyed Blocks

In order to use the keying system, the following "keyed blocks" are
implemented. You can craft each of these shapelessly with a lockset to set its
bitting (essentially, to configure which keys will fit); these blocks _cannot_
be placed until configured (since, in SMP, they need a fitting key to
break--see below). Once keyed with a lockset, the block's tooltip will register
that it is "Keyed", but does not reveal the lockset bitting, so as to keep it
confidential.

**Secure Block**: The most basic keyed block, it is solid and indestructible to
players not in creative mode. It can be punched with a fitting key to break
(more on that below).

**Secure Door**: A door which will only open when a fitting key is used on it.
It can be closed by a falling redstone signal or by using any item (or an open
hand). It can still pop off the ground if the block under it is mined, so it's
recommendable to put it on top of a Secure Block.

**Lidded Button**: A stone button which can be locked, like the Secure Door.
When open, using a non-key item will use the button, and using any key (it
doesn't matter if it fits) will close it. When closed, using a fitting key will
open it.

**Lidded Lever**: Like the Lidded Button, but it's a toggle lever instead.

**Captive Key Latch**: When a fitting key is used on it, it takes one of them
from your hand. The key can be extracted, but only when a redstone signal is
applied. A comparator reading this block outputs full signal when a key is
present.

**Captive Key Switch**: Like the Captive Key Latch, but _provides_ a redstone
signal when a key is present instead. The key can be taken anytime (which turns
off the switch).

Every keyed block can be punched to break it with a fitting key. When broken,
the returned block is unkeyed again; that is, it must be crafted with another
lockset to replace in the world. (This is to avoid unauthorized copying of the
lockset.) Note that the Captive Key devices already _have_ a fitting key inside
of them; they're not meant for security, so much as safety; in particular, they
are designed to be suitable for [trapped-key interlocking][tki]. The lidded
switches are similarly easy to bypass by anyone with a redstone torch or lever.

_What about locking inventories?_ In the aspirations to make this balanced in
SMP, these aren't _yet_ included, and might not be without good reason. A
simple recommendation is to make a small room for a vanilla chest out of Secure
Blocks with a Secure Door.

[tki]: https://en.wikipedia.org/wiki/Trapped-key_interlocking

## Keyrings

To avoid having filling up one's inventory, you can craft a _keyring_ (as of
this writing, it's eight gold nuggets in a ring) which acts like a small-chest
(27 slots) worth of bitted key storage. For almost any keyed block, using (or
punching) it with the keyring will try each key on the ring (in order), and use
the first one that works. Only if _no_ key on the keyring works will the action
fail. The notable exception is the Captive Key devices, which have to steal the
key from you--just pull it out of your keyring first.

## Debug Tools

Mostly for debugging, but also for the utility of server operators and players
in creative mode, a couple utility items are provided. Note that they _both_
irreparably break security, so one should be cautious about spawning these
wherever that is a concern.

**Creative Key**: Get out of the way, locksmithing system--that door's going to
open no matter what! The Creative Key fits every lock (it functions as a member
of all tiers), and can be used anywhere a bitted key is expected (this includes
as a captive key), with the notable exception of a keyring (you don't _need_ a
keyring at that point). It's recommendable for creative players who don't want
to punch out keyed blocks and deal with the headache of replacing them--for
example, in adventure map design.

**Core Extractor**: This handy core ~~drill~~ extractor can be used to pull the
lockset from any configured block in the world, or a configured keyed block in
inventory, by using it on the block in the world, or crafting it with the item
respectively. In the crafting usage, neither the keyed block nor the extractor
is consumed--you simply end up with a copied lockset in the result. This can
prove useful for server admins addressing concerns, or lazy server ops who've
forgotten exactly what bitting they can use on their entry door.

## Recipes

TODO! For now, I recommend either browsing the recipes in
`src/main/resources/assets/locksmithery/recipes`, or installing your favorite
recipe viewer (e.g., JEI).

## Licensing

The code (under src/main/java) is copyright Graham "Grissess" Northup, 2021,
with what I'd like to claim as "substantive changes" from the didactic Forge
template. I provide it under the MIT license; see COPYING for details.

The assets (principally under src/main/resources) are copyright Graham
"Grissess" Northup and Josh Gordon, 2021, and are provided to the public domain
by grant of the CC0 Universal license. See COPYING-ASSETS for details.

## Questions? Comments? Bugs? PRs? Free beer?

Feel free to open an issue or PR on [the Github repository][gh] :)

[gh]: https://github.com/Grissess/Locksmithery
