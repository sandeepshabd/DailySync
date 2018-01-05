package com.sandeepshabd.dailysync.models

/**
 * Created by sandeepshabd on 1/4/18.
 */
data class SpeedControl(var state: State)
data class State(var reported:Reported, var desired: Desired?)
data class Reported(var speed:Int,var overTheLimit:Boolean)
data class Desired(var speed:Int,var overTheLimit:Boolean)
data class Delta(var speed:Int,var overTheLimit:Boolean)