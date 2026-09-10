import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui';
import { LoginForm } from '../features/auth';

/**
 * Login Page view for student authentication.
 */
export const LoginPage: React.FC = () => {
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
          <CardHeader className="pb-4">
            <CardTitle className="text-xl">Welcome back</CardTitle>
            <CardDescription>
              Sign in to your account to continue collaborating
            </CardDescription>
          </CardHeader>
          <CardContent>
            <LoginForm />
          </CardContent>
        </Card>
      </div>
    </main>
  );
};
