package com.example.highschoolmathsolver.presenter

interface IPresenter<View> {
    abstract fun attachView(view: View)

    /**
     * Call to remove/detach the attached view from presenter.
     * This is done to break the memory reference between presenter and view, so the GC will
     * know how to collect them.
     *
     * detachView is called when the view is about to be destroyed by Android framework
     */
    abstract fun detachView()

    /**
     * notify the presenter that view is resumed (onResume on Activity, Fragment)
     */
    abstract fun resume()

    /**
     * notify the presenter that view is paused (onPause on Activity, Fragment)
     */
    abstract fun pause()

    /**
     * notify the presenter that view is destroyed (onDestroy on Activity, Fragment)
     */
    abstract fun destroy()
}