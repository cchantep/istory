# iStory

## Usage

iStory allows find differences between instances, and then to patch or revert. Thereafter an example about strings.

```java
String orig = "original";
String dest = "dest";

// Generating Diff of String
Diff<String> diff = new SimpleStringDiff(orig, dest);

// Patching
String patched = diff.patch(orig); // dest.equals(patched)

// Reverting
String reverted = diff.revert(dest); // orig.equals(reverted)
```

Diff implementations for basic type are provided as following:

- For `Boolean` (yes that's trivial, but better to have it for coherency): `istory.BBooleanDiff`.
- For `Number` (either object types like `Integer`, or primitive type like `int`, thanks to type boxing): `istory.NumberDiff`.
- For `String`: `istory.SimpleStringDiff` or `istory.LCSCharSequenceDiff`. First one doesn't require much processing, but keep in memory both old and new states (which can be what you don't want if goal is to save space). For the second one see after there LCS implementations.

Implementations based on the Long Common Sub-sequence algorithm (inspired from [http://www.bioalgorithms.info](http://www.bioalgorithms.info/downloads/code/LCS.java)) are provided for 'containers' types. These implementations require more processing, but will save more memory ('real' diff).

- For `CharSequence` (including `String`): `istory.LCSCharSequenceDiff`.
- For `java.util.Collection<E>` (and subtypes, including `java.util.SortedSet`): `istory.LCSCollectionDiff<E>`.
- For `java.util.SortedMap<K,V>`: `istory.LCSSortedMapDiff<K,V>` (see after there for unsorted map).

Basic implementations for unsorted maps and sets are following one:

- For `java.util.Set<E>` (unsorted, and subtypes): `istory.UnsortedSetDiff<E>`.
- For `java.util.Map<K,V>` (unsorted, and subtypes): `istory.UnsortedMapDiff<K,V>`.