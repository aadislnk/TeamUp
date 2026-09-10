import React from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { Card, CardContent, CardDescription, CardHeader, CardTitle, Button } from '../components/ui';
import { OtpForm } from '../features/auth';

interface LocationState {
  email?: string;
}

/**
 * OTP Verification Page view for email verification post-registration.
 *
 * Handles presence of registration state email, and renders a fallback UI
 * if accessed directly without registration context.
 */
export const OtpPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const state = location.state as LocationState | null;
  const email = state?.email;

  return (
    <main className="min-h-screen flex flex-col justify-center items-center px-4 py-8 bg-background">
      <div className="w-full max-w-md">
        <div className="text-center mb-6">
          <h1 className="text-2xl font-bold text-foreground tracking-tight">TeamUp</h1>
          <p className="text-sm text-muted mt-1">
            Connect with peers and build teams for campus projects &amp; hackathons
          </p>
        </div>

        <Card className="w-full">
          {email ? (
            <>
              <CardHeader className="pb-4">
                <CardTitle className="text-xl">Verify your email</CardTitle>
                <CardDescription>
                  We sent a 6-digit verification code to{' '}
                  <span className="font-medium text-foreground">{email}</span>. Enter it below to
                  verify your account.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <OtpForm email={email} />
              </CardContent>
            </>
          ) : (
            <>
              <CardHeader className="pb-4">
                <CardTitle className="text-xl">Verification Session Required</CardTitle>
                <CardDescription>
                  No active registration session was found for email verification.
                </CardDescription>
              </CardHeader>
              <CardContent className="flex flex-col gap-4">
                <div
                  role="alert"
                  className="p-3.5 text-sm text-slate-700 bg-slate-50 border border-slate-200 rounded-default"
                >
                  To verify your email address, please start from the registration page or sign in if
                  you already have an account.
                </div>

                <Button
                  type="button"
                  variant="primary"
                  size="md"
                  onClick={() => navigate('/register')}
                  className="w-full"
                >
                  Go to Registration
                </Button>

                <div className="text-center text-sm text-muted pt-1">
                  Already verified?{' '}
                  <Link
                    to="/login"
                    className="text-primary font-medium hover:underline focus:outline-none focus:ring-1 focus:ring-primary rounded-sm"
                  >
                    Log in
                  </Link>
                </div>
              </CardContent>
            </>
          )}
        </Card>
      </div>
    </main>
  );
};
