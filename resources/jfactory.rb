java_import 'tttmiddleware.interfaces.TTTFactory'
require 'ruby_ttt/factory'

class JFactory < Factory
  include TTTFactory

  def getBoard(type)
    get_board(type)
  end

  def getPlayer(player_type, symbol)
    name = nil
    get_player(player_type, symbol)
  end

  def getGame(id, board, player1, player2)
    get_game(id, board, player1, player2)
  end

end