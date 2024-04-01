package org.codemetrics4j.functional

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository

class GitRepository {
	Repository repository

	private GitRepository(Repository repository) {
		this.repository = repository
	}

	/**
	 * Clones a Git repository.
	 *
	 * @param url The URL of the repository to clone.
	 * @param ref The branch, tag, or commit to clone.
	 * @param directory The directory where the repository will be cloned.
	 * @return A `GitRepository` instance representing the cloned repository.
	 * @throws RuntimeException if an error occurs during cloning.
	 */
	static GitRepository clone(String url, String ref, File directory) {
		Git git = Git.cloneRepository()
				.setURI(url)
				.setDirectory(directory)
				.setBranch(ref) // Specify the ref (branch, tag, etc.)
				.setDepth(1) // Shallow clone of the latest
				.setNoTags() // Reduce fetch size if you don't need tags
				.call()
		try {
			return new GitRepository(git.getRepository())
		} catch (Exception e) {
			throw new RuntimeException("Error cloning repository", e)
		} finally {
			git.close()
		}
	}
}
