
package org.mds.net.util.search;

/**
 * Bias used to decide how to resolve ambiguity, for example in a binary search
 * with a list of timestamps - if the requested timestamp lies between two
 * actual data snapshots, should we return the next, previous, nearest one, or
 * none unless there is an exact match.
 *
 * @author
 */
public enum Bias {

    /**
     * If a search result falls between two elements, prefer the next element
     */
    FORWARD,
    /**
     * If a search result falls between two elements, prefer the previous element
     */
    BACKWARD,
    /**
     * If a search result falls between two elements, prefer the element with
     * the minimum distance
     */
    NEAREST,
    /**
     * If a search result falls between two elements, return no element unless
     * there is an exact match
     */
    NONE;
}
