package sourcelookup;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.AbstractSourceContainerTypeDelegate;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.LaunchingMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * // TODO (michael) document me
 * 
 * @author Björn Michael
 * @since 1.0
 */
public class MyMvnSourceContainerTypeDelegate extends AbstractSourceContainerTypeDelegate {

  static final String TYPE_ID = "sourcelookup.myMvnSourceContainerType";

  private static final String MVNCONTAINER = "mvncontainer";
  private static final String PROJECT_NAME = "name";

  @Override
  public ISourceContainer createSourceContainer(final String memento) throws CoreException {
    final Node node = parseDocument(memento);
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      final Element element = (Element) node;
      if (MVNCONTAINER.equals(element.getNodeName())) {
        final String string = element.getAttribute(PROJECT_NAME);
        if (string == null || string.length() == 0) {
          abort(LaunchingMessages.JavaProjectSourceContainerTypeDelegate_5, null);
        }

        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProject project = workspace.getRoot().getProject(string);
        final IJavaProject javaProject = JavaCore.create(project);

        return new MyMvnSourceContainer(javaProject);
      }
      abort(LaunchingMessages.JavaProjectSourceContainerTypeDelegate_6, null);
    }
    abort(LaunchingMessages.JavaProjectSourceContainerTypeDelegate_7, null);

    return null;
  }

  @Override
  public String getMemento(final ISourceContainer container) throws CoreException {
    final MyMvnSourceContainer sourceContainer = (MyMvnSourceContainer) container;
    final Document document = newDocument();
    final Element element = document.createElement(MVNCONTAINER);
    element.setAttribute(PROJECT_NAME, sourceContainer.getProjectName());
    document.appendChild(element);

    return serializeDocument(document);
  }

}
