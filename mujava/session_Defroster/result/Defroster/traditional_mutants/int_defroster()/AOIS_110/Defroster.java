// This is a mutant program.
// Author : ysma

public class Defroster
{

    int We7_BE_LOSGELASSEN = 1;

    int We6_BE_CONFIRM_ON = 1;

    int We12_BLINK_ON = 1;

    int We9_DEF_OUT = 1;

    int We8_BE_CONFIRM_OUT = 1;

    int We5_BE_HANDLING = 1;

    int We11_BLINK_OUT = 1;

    int We2_Clip15_OUT = 1;

    int We3_Clip15_ON = 1;

    int error_e;

    int confirmation_e;

    int Clip_15;

    int ControlElement_DEF;

    int control_led;

    int request;

     void Exception_handler()
    {
        if (0 != We6_BE_CONFIRM_ON) {
            We6_BE_CONFIRM_ON = 0;
        } else {
            if (0 != We7_BE_LOSGELASSEN) {
                We7_BE_LOSGELASSEN = 0;
            }
        }
    }

    int We1_BA_DEF_ev_ctr0 = 0;

    int We1_BA_DEF_ev_ctr1 = 0;

    int We1_BA_DEF_ev_ctr2 = 0;

    int We1_BA_DEF_ev_ctr3 = 0;

    int We1_BA_DEF_ev_ctr5 = 0;

    public  int[] defroster()
    {
        int We1_BA_DEF_ev = 0;
        int We1_BA_DEF;
        We1_BA_DEF_ev_ctr1++;
        We1_BA_DEF_ev_ctr0++;
        if (0 != We2_Clip15_OUT) {
            if (0 != Clip_15) {
                We2_Clip15_OUT = 0;
                We3_Clip15_ON = 1;
                We9_DEF_OUT = 1;
                We1_BA_DEF_ev_ctr0 = 0;
                We11_BLINK_OUT = 1;
                control_led = 0;
            }
        } else {
            We1_BA_DEF = We1_BA_DEF_ev_ctr2 * We1_BA_DEF_ev_ctr2 - (We1_BA_DEF_ev_ctr3 - 1000);
            if (!(We1_BA_DEF == We1_BA_DEF_ev)) {
                We1_BA_DEF_ev_ctr2 = 0;
            }
            if (0 != We3_Clip15_ON) {
                if (Clip_15 == 0) {
                    if (0 != We11_BLINK_OUT) {
                        We11_BLINK_OUT = 0;
                    } else {
                        if (0 != We12_BLINK_ON) {
                            We12_BLINK_ON = 0;
                        }
                    }
                    if (0 != We5_BE_HANDLING) {
                        Exception_handler();
                    } else {
                        if (0 != We8_BE_CONFIRM_OUT) {
                            We8_BE_CONFIRM_OUT = 0;
                        } else {
                            if (0 != We9_DEF_OUT) {
                                We9_DEF_OUT = 0;
                            }
                        }
                    }
                    We3_Clip15_ON = 0;
                    request = 0;
                    control_led = 0;
                    We2_Clip15_OUT = 1;
                } else {
                    if (0 != We5_BE_HANDLING) {
                        if (We1_BA_DEF_ev_ctr1 >= (int) 1000 && confirmation_e == 0 && ControlElement_DEF == 0) {
                            Exception_handler();
                            request = 0;
                            We9_DEF_OUT = 1;
                        } else {
                            if (0 != We6_BE_CONFIRM_ON) {
                                if (ControlElement_DEF == 0) {
                                    We6_BE_CONFIRM_ON = 0;
                                    We7_BE_LOSGELASSEN = 1;
                                }
                            } else {
                                if (0 != We7_BE_LOSGELASSEN) {
                                    if (ControlElement_DEF > 0) {
                                        Exception_handler();
                                        request = 0;
                                        We8_BE_CONFIRM_OUT = 1;
                                    }
                                }
                            }
                        }
                    } else {
                        if (0 != We8_BE_CONFIRM_OUT) {
                            if (ControlElement_DEF == 0) {
                                We8_BE_CONFIRM_OUT = 0;
                                We9_DEF_OUT = 1;
                            }
                        } else {
                            if (0 != We9_DEF_OUT) {
                                if (ControlElement_DEF > 0) {
                                    We9_DEF_OUT = 0;
                                    request = 1;
                                    We1_BA_DEF_ev_ctr1 = 0;
                                    We5_BE_HANDLING = 1;
                                    We6_BE_CONFIRM_ON = 1;
                                }
                            }
                        }
                    }
                    if (0 != We11_BLINK_OUT) {
                        if (--We1_BA_DEF_ev_ctr0 >= (int) 3250 && error_e > 0 && confirmation_e > 0) {
                            We11_BLINK_OUT = 0;
                        } else {
                            if (request > 0 && error_e == 0) {
                                We11_BLINK_OUT = 0;
                            }
                        }
                    } else {
                        if (0 != We12_BLINK_ON) {
                            if (request == 0 && error_e == 0) {
                                We12_BLINK_ON = 0;
                            } else {
                                if (We1_BA_DEF_ev_ctr0 >= (int) 3250 && error_e > 0 && confirmation_e > 0) {
                                    We12_BLINK_ON = 0;
                                }
                            }
                        }
                    }
                }
            } else {
                We2_Clip15_OUT = 1;
            }
        }
        if (We1_BA_DEF_ev_ctr2 + We1_BA_DEF_ev_ctr3 + We1_BA_DEF_ev_ctr5 == 1024) {
            We1_BA_DEF_ev_ctr2 = 32767;
        } else {
            if (We1_BA_DEF_ev_ctr2 - We1_BA_DEF_ev_ctr3 - We1_BA_DEF_ev_ctr5 == 1024) {
                We1_BA_DEF_ev_ctr2 = -32768;
            } else {
                We1_BA_DEF_ev_ctr2 = 32767;
            }
        }
        int[] arr = { We7_BE_LOSGELASSEN, We6_BE_CONFIRM_ON, We12_BLINK_ON, We9_DEF_OUT, We8_BE_CONFIRM_OUT, We5_BE_HANDLING, We11_BLINK_OUT, We2_Clip15_OUT, We3_Clip15_ON, We1_BA_DEF_ev_ctr0, We1_BA_DEF_ev_ctr1, We1_BA_DEF_ev_ctr2, We1_BA_DEF_ev_ctr3, We1_BA_DEF_ev_ctr5, error_e, confirmation_e, Clip_15, ControlElement_DEF, control_led, request };
        return arr;
    }

}
