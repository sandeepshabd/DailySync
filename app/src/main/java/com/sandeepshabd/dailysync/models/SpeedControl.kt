package com.sandeepshabd.dailysync.models

/**
 * Created by sandeepshabd on 1/4/18.
 */
data class SpeedControl(var state: State)
data class State(var reported:Reported, var desired: Desired?)
data class Reported(var speed:Float,var overTheLimit:Boolean, var acceleration:Float)
data class Desired(var speed:Float,var overTheLimit:Boolean, var acceleration:Float)
//data class Delta(var speed:Float,var overTheLimit:Boolean,, var acceleration:Float)