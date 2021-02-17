package com.espressif.idf.ui.handlers;

import java.io.File;

import org.eclipse.cdt.cmake.core.internal.Activator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.launchbar.core.ILaunchBarManager;
import org.eclipse.launchbar.core.ILaunchDescriptor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.espressif.idf.core.logging.Logger;

@SuppressWarnings("restriction")
public class RenameHandler extends RenameParticipant
{

	private boolean deleteDirectory(File directoryToBeDeleted)
	  {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null)
	    {
	      for (File file : allContents)
	      {
	        deleteDirectory(file);
	      }
	    }
	    return directoryToBeDeleted.getName().equals("build") ? true : directoryToBeDeleted.delete();
	  }
	
	@Override
	protected boolean initialize(Object element)
	{
		return true;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
			throws OperationCanceledException
	{
		return null;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		ILaunchBarManager launchBarManager = Activator.getService(ILaunchBarManager.class);
	    ILaunchDescriptor activeLaunchDescriptor = null;
	    try
	    {
	      activeLaunchDescriptor = launchBarManager.getActiveLaunchDescriptor();
	    }
	    catch (CoreException e)
	    {
	      Logger.log(e);
	    }

	    String projectName = activeLaunchDescriptor.getName();
	    IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IResource project = workspace.getRoot().findMember(projectName);
	    File buildLocation = new File(project.getLocation() + "/build"); //$NON-NLS-1$
	    deleteDirectory(buildLocation);
	    project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		return null;
	}
}
