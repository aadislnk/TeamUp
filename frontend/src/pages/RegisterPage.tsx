import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui';
import { RegisterForm } from '../features/auth';

/**
 * Registration Page view for student onboarding.
 */
export const RegisterPage: React.FC = () => {
  return (
    <main className="min-h-screen flex flex-col justify-center items-center px-4 py-8 bg-background">
      <div className="w-full max-w-lg">
        <div className="text-center mb-6">
          <h1 className="text-2xl font-bold text-foreground tracking-tight">TeamUp</h1>
          <p className="text-sm text-muted mt-1">
            Connect with peers and build teams for campus projects &amp; hackathons
          </p>
        </div>

        <Card className="w-full">
          <CardHeader className="pb-4">
            <CardTitle className="text-xl">Create your account</CardTitle>
            <CardDescription>
              Enter your details to register as a student collaborator
            </CardDescription>
          </CardHeader>
          <CardContent>
            <RegisterForm />
          </CardContent>
        </Card>
      </div>
    </main>
  );
};
