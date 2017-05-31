package viewmodel;

import javafx.scene.text.Text;
import org.abego.treelayout.NodeExtentProvider;

public class ASTNodeExtentProvider implements NodeExtentProvider<SimpleAstNode> {

    private final double baseWidth;
    private final double baseHeight;

    public ASTNodeExtentProvider(double baseWidth, double baseHeight) {
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    @Override
	public double getWidth(SimpleAstNode node) {
		return baseWidth + new Text(node.toString()).getBoundsInLocal().getWidth();
	}

	@Override
	public double getHeight(SimpleAstNode node) {
		return baseHeight + new Text(node.toString()).getBoundsInLocal().getHeight();
	}

}