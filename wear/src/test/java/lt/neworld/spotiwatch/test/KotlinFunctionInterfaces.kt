package lt.neworld.spotiwatch.test

/**
 * @author Andrius Semionovas
 * @since 2016-03-28
 *
 * This is workaround for mocking inside Robolectric runner
 * http://stackoverflow.com/q/29072148/312161
 */

interface TestFunction0<R> : Function0<R>