package no.ntnu.utils;


import org.junit.Test;

import static no.ntnu.utils.StringUtils.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class StringUtilsTest {
    String testString = "We are going to jump headlong into creating your first Maven project! " +
            "To create our first Maven project we are going to use Maven's archetype mechanism. " +
            "An archetype is defined as an original pattern or model from which all other things ";
    String s="D.O.A. (1988 film) - Wikipedia, the free encyclopedia D.O.A. (1988 film) From Wikipedia, the free encyclopedia   (Redirected from D O A (1988 film)) Jump to: navigation, search D.O.A. Theatrical release poster Directed by Annabel Jankel, Rocky Morton Produced by Ian Sander, Laura Ziskin Written by Charles Edward Pogue, Russell Rouse, Clarence Greene Starring Dennis Quaid, Meg Ryan, Charlotte Rampling, Daniel Stern Music by Chaz Jankel Cinematography Yuri Neyman Editing by Raja Gosnell, Michael R. Miller Distributed by Touchstone Pictures (USA, theatrical) Release date(s) May 18, 1988 Running time 96 min Country USA Language English IMDb D.O.A. is a 1988 remake of the 1950 film noir of the same name. The film was directed by Annabel Jankel and Rocky Morton, the creators of Max Headroom. It starred Dennis Quaid, Meg Ryan (in one of her first roles), and Charlotte Rampling and featured Timbuk 3 playing one of their songs in a bar scene. [edit] External links D.O.A. at the Internet Movie Database. v ��� d ��� e American films by year Actors �� Directors �� Animation �� Cinematographers �� Composers  �� Editors �� Films A���Z �� Producers �� Screenwriters 1890s �� 1900 �� 1901 �� 1902 �� 1903 �� 1904 �� 1905 �� 1906 �� 1907 �� 1908 �� 1909 �� 1910 �� 1911 �� 1912 �� 1913 �� 1914 �� 1915 �� 1916 �� 1917 �� 1918 �� 1919 �� 1920 �� 1921 �� 1922 �� 1923 �� 1924 �� 1925 �� 1926 �� 1927 �� 1928 �� 1929 �� 1930 �� 1931 �� 1932 �� 1933 �� 1934 �� 1935 �� 1936 �� 1937 �� 1938 �� 1939 �� 1940 �� 1941 �� 1942 �� 1943 �� 1944 �� 1945 �� 1946 �� 1947 �� 1948 �� 1949 �� 1950 �� 1951 �� 1952 �� 1953 �� 1954 �� 1955 �� 1956 �� 1957 �� 1958 �� 1959 �� 1960 �� 1961 �� 1962 �� 1963 �� 1964 �� 1965 �� 1966 �� 1967 �� 1968 �� 1969 �� 1970 �� 1971 �� 1972 �� 1973 �� 1974 �� 1975 �� 1976 �� 1977 �� 1978 �� 1979 �� 1980 �� 1981 �� 1982 �� 1983 �� 1984 �� 1985 �� 1986 �� 1987 �� 1988 �� 1989 �� 1990 �� 1991 �� 1992 �� 1993 �� 1994 �� 1995 �� 1996 �� 1997 �� 1998 �� 1999 �� 2000 �� 2001 �� 2002 �� 2003 �� 2004 �� 2005 �� 2006 �� 2007 �� 2008 �� 2009  This article about a thriller film is a stub. You can help Wikipedia by expanding it. Retrieved from \"http://en.wikipedia.org/wiki/D.O.A._(1988_film)\" Categories: Thriller film stubs | Neo-noir | Film remakes | 1988 films | Touchstone Pictures films | Mystery films | Psychological thriller films | Films shot in New Orleans Views Article Discussion Edit this page History Personal tools Log in / create account Navigation Main page Contents Featured content Current events Random article Search   Interaction About Wikipedia Community portal Recent changes Contact Wikipedia Donate to Wikipedia Help Toolbox What links here Related changes Upload file Special pages Printable version Permanent link Cite this page Languages Deutsch Fran��ais This page was last modified on 23 January 2009, at 07:56. All text is available under the terms of the GNU Free Documentation License. (See Copyrights for details.) Wikipedia® is a registered trademark of the Wikimedia Foundation, Inc., a U.S. registered 501(c)(3) tax-deductible nonprofit charity. Privacy policy About Wikipedia Disclaimers";
    
    @org.junit.Test
    public void testCount() {
        assertEquals(1, count(testString, "jump headlong into creating your first Maven project"));
        assertEquals(3, count(testString, "Maven"));
        assertEquals(1, count(testString, "things"));
    }

    @org.junit.Test
    public void testCountRegex() {
        assertEquals(1, countRegex(testString, "jump headlong into creating your first Maven project"));
        assertEquals(3, countRegex(testString, "Maven"));
        assertEquals(0, countRegex(testString, "maven"));
    }

    @Test
    public void testDeletesAdjacentString() throws Exception{
        assertEquals(-1, deleteAdjacentTokens("Howard/O Hawks/O Fritz/PERSON Lang/PERSON $/O-LCB-/Oe2/O-RCB-/O -LRB-/O1931/DATE-RRB-/O " +
                "Renoir/ORGANIZATION Scenes/ORGANIZATION", "/PERSON")
                .indexOf("Fritz")
        );

        assertEquals(-1, deleteAdjacentTokens("Howard/O Hawks/O Fritz/PERSON Lang/PERSON $/O-LCB-/Oe2/O-RCB-/O -LRB-/O1931/DATE-RRB-/O " +
                "Renoir/ORGANIZATION Scenes/ORGANIZATION", "/ORGANIZATION")
                .indexOf("Renoir")
        );
    }

    @Test
    public void testReturnsCorrectToken() throws Exception{
        assertEquals("Thi", tokenReverse("This is str", 3));
        assertEquals("This", tokenReverse("This is str", 4));
        assertEquals("This ", tokenReverse("This is str", 5));
        assertEquals("is", tokenReverse("This is str", 7));
        assertEquals("Obama", tokenReverse("Barack Obama is the 44th President", 12));

        assertEquals("Fritz", tokenReverse("Scarface/O -LRB-/O1932/DATE-RRB-/O dir/O./O " +
                "Howard/O Hawks/O $/O-LCB-/Oe1/O-RCB-/O -LRB-/O1945/DATE-RRB-/O dir/O./O " +
                "Fritz/PERSON Lang/PERSON $/O-LCB-/Oe2/O-RCB-/O -LRB-/O1931/DATE-RRB-/O " +
                "dir/O./O Jean/O Renoir/ORGANIZATION Scenes/ORGANIZATION", 121));


    }

    @Test
    public void testMaskOccurrences() throws Exception {
    	maskOccurrences(s, "${e1}", Col.toCol("d.o.a."));
	}
}
