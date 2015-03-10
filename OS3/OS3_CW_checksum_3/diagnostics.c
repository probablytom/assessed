/* Author:  Peter Dickman
 * Editted: 23-10-95
 * Version: 1.00
 *
 * Impl file for RUNTIMEDIAGNOSTICS stuff 
 */

#include "diagnostics.h"

/* By default don't output diagnostics if run-time mode is used         */
/* NB: output is always to stdout, no option to change this is provided */

static int diagnostics_state = 0;

int  diagnostics_enabled(void)
{ return diagnostics_state; }

void enable_diagnostics(void)
{ diagnostics_state = 1; }

void disable_diagnostics(void)
{ diagnostics_state = 0; }

